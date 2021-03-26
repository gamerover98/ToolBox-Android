package it.uniba.magr.misurapp.tool.barometer;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.navigation.NavController;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class BarometerNavigation implements Navigable, SensorEventListener {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    private static final float SEA_PRESSURE = 1013f;

    /**
     * The navigation bundle pressure key.
     */
    public static final String BUNDLE_PRESSURE_KEY = "pressure";

    /**
     * The activity instance.
     */
    private Activity activity;

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * Pressure sensor that will be initialized.
     */
    private Sensor pressureSensor;

    /**
     * Barometer indicator view.
     */
    private BarometerView barometerView;

    /**
     * The barometer value text view.
     */
    private TextView barometerTextView;

    /**
     * The height value text view.
     */
    private TextView heightTextView;

    /**
     * The current sensor pressure.
     */
    private float currentPressure;

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

        this.activity = activity;

        FloatingActionButton saveButton = activity.findViewById(R.id.barometer_fab_button_save);
        saveButton.setOnClickListener(view -> onSaveButtonClick(activity));

        sensorManager  = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        assert pressureSensor != null;

        barometerView     = activity.findViewById(R.id.barometer_view);
        barometerTextView = activity.findViewById(R.id.barometer_pressure_value);
        heightTextView    = activity.findViewById(R.id.barometer_height_value);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float[] values = sensorEvent.values;
        float pressure = values[0];

        this.currentPressure = pressure;
        float height = SensorManager.getAltitude(SEA_PRESSURE, pressure);

        String barometerUnit = activity.getString(R.string.barometer_unit);
        String heightUnit    = activity.getString(R.string.height_unit);

        String barometerText = DECIMAL_FORMAT.format(currentPressure) + " " + barometerUnit;
        String heightText    = DECIMAL_FORMAT.format(height) + " " + heightUnit;

        barometerView.setCurrentPressure(pressure);

        barometerTextView.setText(barometerText);
        heightTextView.setText(heightText);

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

    /**
     * When clicking the save button.
     * @param context The application context.
     */
    private void onSaveButtonClick(@NotNull Context context) {

        HomeActivity homeActivity = (HomeActivity) context;

        Bundle bundle = new Bundle();
        bundle.putFloat(BUNDLE_PRESSURE_KEY, currentPressure);

        NavController navController = homeActivity.getNavController();
        navController.navigate(R.id.action_nav_save_measure_fragment_to_barometer, bundle);

    }

}
