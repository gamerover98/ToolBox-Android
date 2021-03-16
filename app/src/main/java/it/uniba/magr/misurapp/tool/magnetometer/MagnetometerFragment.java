package it.uniba.magr.misurapp.tool.magnetometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.navigation.ToolNavigationFragment;

public class MagnetometerFragment extends ToolNavigationFragment {

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * The magnetometer sensor instance from the system.
     */
    private Sensor magnetometerSensor;

    /**
     * The magnetometer sensor event that manipulates the tool interface
     * with properly info and data.
     */
    private MagnetometerSensorEvent magnetometerSensorEvent;

    public MagnetometerFragment() {
        super(new MagnetometerNavigation());
    }

    @Override
    public void onAttach(@NotNull Context context) {

        super.onAttach(context);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    public void onResume() {

        super.onResume();

        magnetometerSensorEvent = new MagnetometerSensorEvent(this);
        sensorManager.registerListener(magnetometerSensorEvent, magnetometerSensor,
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onPause() {

        super.onPause();
        sensorManager.unregisterListener(magnetometerSensorEvent);

    }

    /*
     * Disable fullscreen mode.
     */
    @Override
    protected void fullscreen(@NotNull Activity activity) {
        // Nothing to do.
    }

}
