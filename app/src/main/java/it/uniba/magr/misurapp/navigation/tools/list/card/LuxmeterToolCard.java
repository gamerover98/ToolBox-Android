package it.uniba.magr.misurapp.navigation.tools.list.card;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.navigation.NavController;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;

public class LuxmeterToolCard implements ToolCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_luxmeter;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_luxmeter;
    }

    @Override
    public void onClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;
        NavController navController = activity.getNavController();

        navController.navigate(R.id.action_nav_add_measure_fragment_to_luxmeter);

    }

    @Override
    public boolean isSupported(@NotNull Context context) {

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor luxmeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        return luxmeterSensor != null;

    }

}
