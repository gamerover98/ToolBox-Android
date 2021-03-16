package it.uniba.magr.misurapp.tool.magnetometer;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
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
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class MagnetometerNavigation implements Navigable, SensorEventListener {

    private final static int MAGNETIC_SENSOR_MAX_X = 1000;
    private final static int MAGNETIC_SENSOR_MAX_Y = 1000;
    private final static int MAGNETIC_SENSOR_MAX_Z = 1000;

    private static final int MAGNETIC_SENSOR_MAX_VALUE = (int) Math.sqrt(Math.pow(
            MAGNETIC_SENSOR_MAX_X,2) + Math.pow(MAGNETIC_SENSOR_MAX_Y,2) + Math.pow(
            MAGNETIC_SENSOR_MAX_Z,2));

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * The magnetometer sensor instance from the system.
     */
    private Sensor magnetometerSensor;

    private TextView value;
    private LineChart lineChart;
    private Thread thread;
    private ProgressBar progressBar;

    private boolean plotData = true;

    public static DecimalFormat DECIMAL_FORMATTER;

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

        if (activity instanceof HomeActivity) {

            sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
            magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            if(magnetometerSensor != null){
                sensorManager.registerListener(this, magnetometerSensor,
                        SensorManager.SENSOR_DELAY_GAME);
            }

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setDecimalSeparator('.');
            DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);

            value = activity.findViewById(R.id.tesla_value);
            progressBar = activity.findViewById(R.id.magnetometer_progress_bar);

            lineChart = (LineChart) activity.findViewById(R.id.chart_metal_detector);
            lineChart.getDescription().setEnabled(false);
            lineChart.setTouchEnabled(false);
            lineChart.setDragEnabled(false);
            lineChart.setScaleXEnabled(false);
            lineChart.setDrawGridBackground(false);
            lineChart.setPinchZoom(false);

            //inizializzazione grafico vuoto
            LineData data = new LineData();
            data.setValueTextColor(Color.WHITE);
            lineChart.setData(data);

            //Parametri per la leggenda
            Legend l = lineChart.getLegend();

            l.setForm(Legend.LegendForm.LINE);
            l.setTextSize(15f);
            l.setTextColor(Color.BLACK);

            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.setTextColor(Color.BLACK);
            leftAxis.setTextSize(15);
            leftAxis.setAxisMaximum(magnetometerSensor.getMaximumRange());
            leftAxis.setAxisMinimum(0f);
            leftAxis.setDrawZeroLine(true);
            leftAxis.setDrawGridLines(true);

            YAxis rightAxis = lineChart.getAxisRight();
            rightAxis.setEnabled(false);

            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.setDrawBorders(true);

            startPlot();

        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        progressBar.setMax(MAGNETIC_SENSOR_MAX_VALUE);

        float magX = sensorEvent.values[0];
        float magY = sensorEvent.values[1];
        float magZ = sensorEvent.values[2];

        double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));

        magnitude*=1000;
        magnitude = Math.floor(magnitude);
        magnitude /=1000;

        value.setText(String.format("%s\n\t\tµTesla", magnitude));

        int color = Color.GREEN;

        if(magnitude >= 500 && magnitude <= 1000 ){
            color = Color.YELLOW;
        }

        if(magnitude > 1000 ){
            color = Color.RED;
        }

        progressBar.setProgress((int) magnitude);
        progressBar.setProgressTintList(ColorStateList.valueOf(color));
        if(plotData){
            addEntry((float) magnitude);
            plotData = false;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do.
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Tesla values");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(5f);
        set.setColor(Color.BLUE);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(true);
        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(7f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;

    }

    private void addEntry(float magnitude) {

        LineData data = lineChart.getData();
        if(data != null){
            ILineDataSet set = data.getDataSetByIndex(0);

            if(set == null){
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), magnitude), 0);
            data.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(7);
            lineChart.moveViewToX(data.getEntryCount());

        }
    }

    private void startPlot() {

        if(thread != null){
            thread.interrupt();
        }

        thread = new Thread(() -> {
            while (true){
                plotData = true;
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    @Override
    public void onResume() {

        Navigable.super.onResume();

        sensorManager.registerListener(this, magnetometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {

        Navigable.super.onPause();

        if(thread != null){
            thread.interrupt();
        }

        sensorManager.unregisterListener(this);
    }
}
