package it.uniba.magr.toolbox.navigation.tools.list.card;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.navigation.NavController;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.HomeActivity;
import it.uniba.magr.toolbox.R;

public class CompassToolCard implements ToolCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_compass;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_compass;
    }

    @Override
    public void onClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;
        NavController navController = activity.getNavController();

        navController.navigate(R.id.action_nav_add_measure_fragment_to_compass);

    }

    @Override
    public boolean isSupported(@NotNull Context context) {

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return accelerometerSensor != null && magneticSensor != null;

    }

}
