package it.uniba.magr.misurapp.navigation.tools.list.card;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.navigation.NavController;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;

public class MagnetometerToolCard implements ToolCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_magnetometer;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_magnetometer;
    }

    @Override
    public void onClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;
        NavController navController = activity.getNavController();

        navController.navigate(R.id.action_nav_add_measure_fragment_to_magnetometer);

    }

    @Override
    public boolean isSupported(@NotNull Context context) {

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return magnetometerSensor != null;

    }

}
