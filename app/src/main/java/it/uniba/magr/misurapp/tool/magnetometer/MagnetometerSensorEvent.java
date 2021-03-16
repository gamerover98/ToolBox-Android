package it.uniba.magr.misurapp.tool.magnetometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import org.jetbrains.annotations.NotNull;

public class MagnetometerSensorEvent {

    /**
     * The not null magnetometer fragment instance.
     */
    @NotNull
    private final MagnetometerFragment magnetometerFragment;

    public MagnetometerSensorEvent(@NotNull MagnetometerFragment magnetometerFragment) {
        this.magnetometerFragment = magnetometerFragment;
    }

}
