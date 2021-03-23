package it.uniba.magr.misurapp.tool.barometer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class BarometerNavigation implements Navigable, SensorEventListener {

    private static final float MIN_PRESSURE = 900f;
    private static final float MAX_PRESSURE = 1100f;

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * Pressure sensor that will be initialized.
     */
    private Sensor pressureSensor;

    /**
     * Pressure indicator view.
     */
    private HalfGauge halfGauge;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_barometer;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_barometer);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        sensorManager  = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        assert pressureSensor != null;

        halfGauge = activity.findViewById(R.id.barometer_gauge);
        halfGauge.setMinValue(MIN_PRESSURE);
        halfGauge.setMaxValue(MAX_PRESSURE);

        Range yellowRange = new Range();
        Range greenRange  = new Range();
        Range redRange    = new Range();

        yellowRange.setColor(Color.YELLOW);
        yellowRange.setFrom(MIN_PRESSURE);
        yellowRange.setTo(950f);

        greenRange.setColor(Color.GREEN);
        greenRange.setFrom(yellowRange.getTo());
        greenRange.setTo(1050f);

        redRange.setColor(Color.RED);
        redRange.setFrom(greenRange.getTo());
        redRange.setTo(MAX_PRESSURE);

        halfGauge.setRanges(Arrays.asList(
                greenRange, yellowRange, redRange
        ));

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float[] values = sensorEvent.values;
        float pressure = values[0];

        halfGauge.setValue(pressure);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do.
    }

    @Override
    public void onResume() {

        sensorManager.registerListener(this, pressureSensor,
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
    }



}
