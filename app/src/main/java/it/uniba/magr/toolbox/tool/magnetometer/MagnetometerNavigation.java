package it.uniba.magr.toolbox.tool.magnetometer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import it.uniba.magr.toolbox.HomeActivity;
import it.uniba.magr.toolbox.R;
import it.uniba.magr.toolbox.navigation.Navigable;
import it.uniba.magr.toolbox.tool.util.XAxisValueFormatterUtil;

public class MagnetometerNavigation implements Navigable, SensorEventListener {

    /**
     * The navigation bundle seconds key.
     */
    public static final String BUNDLE_SECONDS_KEY = "seconds";

    /**
     * The navigation bundle values key.
     */
    public static final String BUNDLE_VALUES_KEY = "values";

    /**
     * The tesla bound to be added into the plot value to avoid pinnacles.
     */
    public static final float VALUE_BOUND = 100;

    /**
     * The magnetic sensor max x value.
     */
    public static final int MAGNETIC_SENSOR_MAX_X = 1000;

    /**
     * The magnetic sensor max y value.
     */
    public static final int MAGNETIC_SENSOR_MAX_Y = 1000;

    /**
     * The magnetic sensor max z value.
     */
    public static final int MAGNETIC_SENSOR_MAX_Z = 1000;

    /**
     * The magnetic max value.
     */
    public static final int MAGNETIC_SENSOR_MAX_VALUE = (int) Math.sqrt(Math.pow(
            MAGNETIC_SENSOR_MAX_X,2) + Math.pow(MAGNETIC_SENSOR_MAX_Y,2) + Math.pow(
            MAGNETIC_SENSOR_MAX_Z,2));

    /**
     * The minimum tesla plot value.
     */
    private static final float MIN_PLOT_VALUE = 10;

    /**
     * The home activity.
     */
    private HomeActivity activity;

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * The magnetometer sensor instance from the system.
     */
    private Sensor magnetometerSensor;

    /**
     * Tesla indicator view.
     */
    private ArcGauge arcGauge;

    /**
     * The LineChart view.
     */
    private LineChart lineChart;

    /**
     * The recording button view.
     */
    private MaterialButton recordingButton;

    /**
     * The Map (seconds, tesla) of the recording values.
     */
    private final Map<Integer, Float> recordingValues = new HashMap<>();

    /**
     * The recording flowing time in seconds.
     */
    private int recordingSeconds;

    /**
     * The recording flag.
     */
    private boolean isRecording = false;

    /**
     * The async thread that provide to update every second the chart.
     */
    private Thread thread;

    /**
     * The max value for the plot.
     * By default, its value is MIN_PLOT_VALUE.
     */
    private float plotMaxValue = MIN_PLOT_VALUE;

    /**
     * If true, the value will be added into the cart.
     * <p>This value will be set true from the async thread.</p>
     */
    private final AtomicBoolean plotData = new AtomicBoolean(true);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_magnetometer;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_magnetometer);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        this.activity      = (HomeActivity) activity;
        sensorManager      = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        assert magnetometerSensor != null; // prevents that the sensor is null

        sensorManager.registerListener(this,
                magnetometerSensor, SensorManager.SENSOR_DELAY_GAME);

        arcGauge = activity.findViewById(R.id.magnetometer_gauge);

        arcGauge.setValueColor(Color.WHITE);
        arcGauge.setMinValue(0);
        arcGauge.setMaxValue(MAGNETIC_SENSOR_MAX_VALUE);

        Range greenRange  = new Range();
        Range yellowRange = new Range();
        Range redRange    = new Range();
        Range maxRange    = new Range();

        greenRange.setColor(Color.GREEN);
        greenRange.setFrom(0);
        greenRange.setTo(1000);

        yellowRange.setColor(Color.YELLOW);
        yellowRange.setFrom(greenRange.getTo());
        yellowRange.setTo(1500);

        redRange.setColor(Color.RED);
        redRange.setFrom(yellowRange.getTo());
        redRange.setTo(MAGNETIC_SENSOR_MAX_VALUE - 10f);

        maxRange.setColor(Color.rgb(139, 0, 0));
        maxRange.setFrom(redRange.getTo());
        maxRange.setTo(MAGNETIC_SENSOR_MAX_VALUE);

        arcGauge.setRanges(Arrays.asList(
                greenRange, yellowRange, redRange, maxRange
        ));

        lineChart = activity.findViewById(R.id.magnetometer_line_chart);
        lineChart.setDrawBorders(true);

        recordingButton = activity.findViewById(R.id.magnetometer_recording_button);
        recordingButton.setOnClickListener(this :: performRecordingButton);

    }

    @Override
    public void onStart() {

        recordingValues.clear();
        recordingSeconds = 0;

    }

    @Override
    public void onResume() {

        sensorManager.registerListener(this, magnetometerSensor,
                SensorManager.SENSOR_DELAY_GAME);

        initLineChart();
        startPlotThread();

    }

    @Override
    public void onPause() {

        sensorManager.unregisterListener(this);

        recordingValues.clear();
        recordingSeconds = 0;

        if (thread != null) {
            thread.interrupt();
        }

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float magX = sensorEvent.values[0];
        float magY = sensorEvent.values[1];
        float magZ = sensorEvent.values[2];

        double magnitude = calculateMagnitude(magX, magY, magZ);

        magnitude *= 1000;
        magnitude  = Math.floor(magnitude);
        magnitude /= 1000;

        if (plotData.get()) {


            if (isRecording) {
                recordingValues.put(recordingSeconds++, (float) magnitude);
            }

            arcGauge.setValue(magnitude);

            if (plotMaxValue < magnitude) {

                plotMaxValue = (float) magnitude;
                updateLineChart();

            }

            addChartValue(magnitude);
            plotData.set(false);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do.
    }

    private void initLineChart() {

        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(false);

        // empty chart initialization
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        lineChart.setData(data);

        Legend legend = lineChart.getLegend();

        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(15f);
        legend.setTextColor(Color.BLACK);

        updateLineChart();

    }

    private void updateLineChart() {

        YAxis leftAxis  = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();
        XAxis xAxis     = lineChart.getXAxis();

        leftAxis.setTextColor    (Color.BLACK);
        leftAxis.setTextSize     (20);
        leftAxis.setAxisMaximum  (plotMaxValue + VALUE_BOUND);
        leftAxis.setAxisMinimum  (-10f);
        leftAxis.setDrawZeroLine (true);
        leftAxis.setDrawGridLines(true);
        rightAxis.setEnabled     (false);
        xAxis.setDrawGridLines   (false);
        xAxis.setValueFormatter  (new XAxisValueFormatterUtil());
        lineChart.setDrawBorders (true);

    }

    /**
     * Gets the magnitude value from the sensor axis values.
     *
     * @param magX The X sensor value.
     * @param magY The Y sensor value.
     * @param magZ The Z sensor value.
     * @return The magnitude value.
     */
    private double calculateMagnitude(float magX, float magY, float magZ) {
        return Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));
    }

    /**
     * Add a value to the chart.
     * @param value The value that will be added to the chart.
     */
    private void addChartValue(double value) {

        LineData data = lineChart.getData();
        ILineDataSet set = data.getDataSetByIndex(0);

        if (set == null) {

            set = createSet();
            data.addDataSet(set);

        }

        data.addEntry(new Entry(set.getEntryCount(), (float) value), 0);
        data.notifyDataChanged();

        lineChart.notifyDataSetChanged();
        lineChart.setVisibleXRangeMaximum(7);
        lineChart.moveViewToX(data.getEntryCount());

    }

    /**
     * @return The not null chart line data with its decorations.
     */
    @NotNull
    private LineDataSet createSet() {

        Context context = lineChart.getContext();
        Resources resources = context.getResources();

        int cardColor = ResourcesCompat.getColor(context.getResources(),
                R.color.magnetometer_card, context.getTheme());

        LineDataSet lineDataSet = new LineDataSet(null,
                resources.getString(R.string.tesla_chart_label));

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(2f);

        lineDataSet.setColor(cardColor);
        lineDataSet.setHighlightEnabled(false);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);

        lineDataSet.setCircleColor(cardColor);
        lineDataSet.setCircleRadius(2f);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);

        lineDataSet.setFillFormatter(new DefaultFillFormatter());
        lineDataSet.setFillColor(cardColor);

        return lineDataSet;

    }

    /**
     * Perform the recording click button.
     * @param view The not null button view.
     */
    private void performRecordingButton(@NotNull View view) {

        Resources resources = activity.getResources();
        Resources.Theme theme = activity.getTheme();

        int recordingColor;
        String recordingText;

        if (!isRecording) {

            isRecording = true;

            recordingColor = ResourcesCompat.getColor(resources, R.color.danger, theme);
            recordingText  = resources.getString(R.string.text_stop_recording);

        } else {

            isRecording = false;

            recordingColor = ResourcesCompat.getColor(resources, R.color.primary, theme);
            recordingText  = resources.getString(R.string.text_start_recording);

        }

        recordingButton.setBackgroundColor(recordingColor);
        recordingButton.setText(recordingText);

        if (!isRecording && !recordingValues.isEmpty()) {

            int size = recordingValues.size();
            int count = 0;

            int[] seconds = new int[size];
            float[] values = new float[size];

            for (Map.Entry<Integer, Float> entry : recordingValues.entrySet()) {

                seconds[count] = entry.getKey();
                values[count]  = entry.getValue();

                count++;

            }

            Bundle bundle = new Bundle();
            bundle.putIntArray(BUNDLE_SECONDS_KEY, seconds);
            bundle.putFloatArray(BUNDLE_VALUES_KEY, values);

            NavController navController = activity.getNavController();
            navController.navigate(R.id.action_nav_save_measure_fragment_to_magnetometer, bundle);

        }

    }

    /**
     * Stop the current update thread if it is started and restart it.
     */
    private void startPlotThread() {

        stopPlotThread();

        thread = new Thread(this :: plotUpdate);
        thread.start();

    }

    /**
     * Stop the current plot update thread with an interrupt.
     */
    private void stopPlotThread() {

        if (isPlotThreadStarted()) {
            thread.interrupt();
        }

    }

    /**
     * @return True if the plot update thread is started.
     */
    private boolean isPlotThreadStarted() {
        return thread != null;
    }

    /**
     * Updates every second the plot data flag.
     */
    private void plotUpdate() {

        boolean interrupted = false;

        while (!interrupted) {

            try {

                plotData.set(true);
                Thread.sleep(1000L);

            } catch (InterruptedException ex) {

                plotData.set(false);
                interrupted = true;

                Thread.currentThread().interrupt();

            }

        }

    }

}
