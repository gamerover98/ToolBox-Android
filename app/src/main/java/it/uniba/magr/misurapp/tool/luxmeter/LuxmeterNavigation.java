package it.uniba.magr.misurapp.tool.luxmeter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class LuxmeterNavigation implements Navigable, SensorEventListener {

    /**
     * The minimum light plot value.
     */
    private static final float MIN_PLOT_VALUE = 10;

    /**
     * The light bound to be added into the plot value to avoid pinnacles.
     */
    private static final float VALUE_BOUND = 100;

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * The luxmeter sensor instance from the system.
     */
    private Sensor luxmeterSensor;

    /**
     * The TextView lux value.
     */
    private TextView luxValueTextView;

    /**
     * The LineChart view.
     */
    private LineChart lineChart;

    /**
     * The ProgressBar view.
     */
    private ProgressBar progressBar;

    /**
     * The async thread that provide to update every second the chart.
     */
    private Thread thread;

    /**
     * If true, the value will be added into the cart.
     * <p>This value will be set true from the async thread.</p>
     */
    private final AtomicBoolean plotData = new AtomicBoolean(true);

    /**
     * The latest max value for the plot Y axis.
     */
    private float currentMaxValue = MIN_PLOT_VALUE;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_luxmeter;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_luxmeter);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        sensorManager      = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        luxmeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        assert luxmeterSensor != null; // prevents that the sensor is null

        sensorManager.registerListener(this,
                luxmeterSensor, SensorManager.SENSOR_DELAY_GAME);

        luxValueTextView = activity.findViewById(R.id.light_value);
        progressBar = activity.findViewById(R.id.luxmeter_progress_bar);
        lineChart   = activity.findViewById(R.id.chart_light_detector);

        progressBar.setMax((int) luxmeterSensor.getMaximumRange());

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float value = sensorEvent.values[0];
        luxValueTextView.setText(String.valueOf(value));

        progressBar.setProgress((int) value);

        if (plotData.get()) {

            if (currentMaxValue < value) {

                currentMaxValue = (float) value;
                updateLineChart();

            }

            addChartValue(value);
            plotData.set(false);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do.
    }

    @Override
    public void onResume() {

        sensorManager.registerListener(this, luxmeterSensor,
                SensorManager.SENSOR_DELAY_GAME);

        initLineChart();
        startPlotThread();

    }

    @Override
    public void onPause() {

        if (thread != null) {
            thread.interrupt();
        }

        sensorManager.unregisterListener(this);

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
        legend.setTextSize(13f);
        legend.setTextColor(Color.BLACK);

        updateLineChart();

    }

    private void updateLineChart() {

        YAxis leftAxis  = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();
        XAxis xAxis     = lineChart.getXAxis();

        leftAxis.setTextColor    (Color.BLACK);
        leftAxis.setTextSize     (20);
        leftAxis.setAxisMaximum  (currentMaxValue + VALUE_BOUND);
        leftAxis.setAxisMinimum  (-10f);
        leftAxis.setDrawZeroLine (true);
        leftAxis.setDrawGridLines(true);
        rightAxis.setEnabled     (false);
        xAxis.setDrawGridLines   (false);
        lineChart.setDrawBorders (true);

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

        LineDataSet lineDataSet = new LineDataSet(null, resources.getString(R.string.light_chart_label));

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(5f);
        lineDataSet.setColor(Color.rgb(255, 165, 0));
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setCircleRadius(7f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);

        return lineDataSet;

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
