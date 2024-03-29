package it.uniba.magr.toolbox.navigation.tools.list.card;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.navigation.NavController;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.HomeActivity;
import it.uniba.magr.toolbox.R;

public class LuxmeterToolCard implements ToolCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_lux_meter;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_lux_meter;
    }

    @Override
    public void onClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;
        NavController navController = activity.getNavController();

        navController.navigate(R.id.action_nav_add_measure_fragment_to_lux_meter);

    }

    @Override
    public boolean isSupported(@NotNull Context context) {

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor luxMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        return luxMeterSensor != null;

    }

}
