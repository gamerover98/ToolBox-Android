package it.uniba.magr.misurapp.tool.pedometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class PedometerNavigation implements Navigable, SensorEventListener {

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * The pedometer sensor instance from the system.
     */
    private Sensor pedometerSensor;

    /**
     * The TextView step value.
     */
    private TextView stepValueTextView;

    /**
     * The steps when this tool is in use.
     *
     * <p>
     *     This field is used to prevents the "non-reset" step count
     *     from the sensor values.
     * </p>
     *
     */
    private int steps;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pedometer;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_pedometer);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        sensorManager   = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        pedometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        assert pedometerSensor != null; // prevents that the sensor is null

        sensorManager.registerListener(this,
                pedometerSensor, SensorManager.SENSOR_DELAY_GAME);

        stepValueTextView = activity.findViewById(R.id.show_steps);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        stepValueTextView.setText(String.valueOf(steps++));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do.
    }

    @Override
    public void onResume() {
        sensorManager.registerListener(this, pedometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
    }

}
