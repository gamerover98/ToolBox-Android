package it.uniba.magr.toolbox.tool.luxmeter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

import it.uniba.magr.toolbox.R;
import it.uniba.magr.toolbox.navigation.Navigable;
import it.uniba.magr.toolbox.tool.util.XAxisValueFormatterUtil;

public class LuxMeterNavigation implements Navigable, SensorEventListener {

    /**
     * The name of the inside path of the lamp that it will be "illuminated".
     */
    private static final String VECTOR_LAMP_BULB_PATH_NAME = "bulb";

    /**
     * The minimum lux plot value.
     */
    private static final float MIN_PLOT_VALUE = 10;
    private static final float VALUE_BOUND = 100;

    /**
     * The max value for the plot.
     * By default, its value is MIN_PLOT_VALUE.
     */
    private float plotMaxValue = MIN_PLOT_VALUE;

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * The luxMeter sensor instance from the system.
     */
    private Sensor luxMeterSensor;

    /**
     * The TextView lux value.
     */
    private TextView luxValueTextView;

    /**
     * The image view that contains the drawable vector
     */
    private ImageView lampImage;

    /**
     * The LineChart view.
     */
    private LineChart lineChart;

    /**
     * The async thread that provide to update every second the chart.
     */
    private Thread thread;

    /**
     * If true, the value will be added into the cart.
     * <p>This value will be set true from the async thread.</p>
     */
    private final AtomicBoolean plotData = new AtomicBoolean(true);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_luxmeter;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_lux_meter);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        sensorManager  = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        luxMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        assert luxMeterSensor != null; // prevents that the sensor is null

        sensorManager.registerListener(this,
                luxMeterSensor, SensorManager.SENSOR_DELAY_GAME);

        luxValueTextView = activity.findViewById(R.id.light_value);
        lineChart        = activity.findViewById(R.id.lux_meter_line_chart_light);
        lampImage        = activity.findViewById(R.id.lux_meter_lamp_image);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Context context = lampImage.getContext();

        // the current light value.
        float currentMaxValue = sensorEvent.values[0];

        String luxUnit = context.getString(R.string.light_unit);
        String luxStringValue = currentMaxValue + " " + luxUnit;

        luxValueTextView.setText(luxStringValue);

        // edit the bulb lamp color with properly "light" color.
        VectorChildFinder vector = new VectorChildFinder(context, R.drawable.lux_meter_lamp, lampImage);
        VectorDrawableCompat.VFullPath bulbVectorPath = vector.findPathByName(VECTOR_LAMP_BULB_PATH_NAME);
        bulbVectorPath.setFillColor(getLampBulbColor(currentMaxValue));

        if (plotData.get()) {

            if (plotMaxValue < currentMaxValue) {

                plotMaxValue = currentMaxValue;
                updateLineChart();

            }

            addChartValue(currentMaxValue);
            plotData.set(false);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do.
    }

    @Override
    public void onResume() {

        sensorManager.registerListener(this, luxMeterSensor,
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

        LineDataSet lineDataSet = new LineDataSet(null, resources.getString(R.string.light_chart_label));

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
     * Gets the light color of the lamp inside.
     *
     * @param luxValue the sensor lux value.
     * @return The color of the light.
     */
    private int getLampBulbColor(float luxValue) {

        float proportionValue = (luxValue / luxMeterSensor.getMaximumRange());
        return ColorUtils.HSLToColor(new float[]{54f, 1f, proportionValue});

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
