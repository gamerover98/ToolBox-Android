package it.uniba.magr.misurapp.navigation.tools.list.card;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;

import static it.uniba.magr.misurapp.HomeActivity.ACTIVITY_RECOGNITION_PERMISSION;

public class PedometerToolCard implements ToolCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_pedometer;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_pedometer;
    }

    @Override
    public void onClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;
        NavController navController = activity.getNavController();

        navController.navigate(R.id.action_nav_add_measure_fragment_to_pedometer);

    }

    @Override
    public boolean isSupported(@NotNull Context context) {

        // Check if both the permission and the sensor are given/supported
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor pedometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        boolean isPermissionGranted = ContextCompat.checkSelfPermission(context,
                ACTIVITY_RECOGNITION_PERMISSION) == PackageManager.PERMISSION_GRANTED;

        return pedometerSensor != null && isPermissionGranted;

    }

}
