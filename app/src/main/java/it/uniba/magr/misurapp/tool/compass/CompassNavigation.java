package it.uniba.magr.misurapp.tool.compass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class CompassNavigation implements Navigable, SensorEventListener {

    private static final int COOLDOWN = 250;

    /**
     * The sensor manager instance from the application context.
     */
    private SensorManager sensorManager;

    /**
     * The accelerometer sensor instance from the system.
     */
    private Sensor accelerometerSensor;

    /**
     * The magnetic field sensor instance from the system.
     */
    private Sensor magneticSensor;

    /**
     * The TextView degrees value.
     */
    private TextView degreesValueTextView;

    /**
     * The image view that contains the drawable vector
     */
    private ImageView compassImage;


    //variabili bussola e matrice di rotazione
    float[] lastAccellerometer = new float[3];
    float[] lastMagnetometer = new float[3];
    float[] rotationMatrix = new float[9];
    float[] orientation = new float[3];

    boolean isLastAccelerometerArrayCopied = false;
    boolean isLastMagnetometerArrayCopied = false;

    long lastUpdatedTime = 0;
    float currentDegree = 0f;



    @Override
    public int getLayoutId() {
        return R.layout.fragment_compass;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_compass);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        sensorManager  = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        assert accelerometerSensor != null && magneticSensor != null; // prevents that the sensor is null

        sensorManager.registerListener(this,
                accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        sensorManager.registerListener(this,
                magneticSensor, SensorManager.SENSOR_DELAY_GAME);

        degreesValueTextView = activity.findViewById(R.id.degrees);
        compassImage        = activity.findViewById(R.id.compass_image);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor == accelerometerSensor) {
            System.arraycopy(sensorEvent.values, 0, lastAccellerometer, 0, sensorEvent.values.length);
            isLastAccelerometerArrayCopied = true;
        } else if (sensorEvent.sensor == magneticSensor) {
            System.arraycopy(sensorEvent.values, 0, lastMagnetometer, 0, sensorEvent.values.length);
            isLastMagnetometerArrayCopied = true;
        }

        if(isLastAccelerometerArrayCopied && isLastMagnetometerArrayCopied &&
                System.currentTimeMillis() - lastUpdatedTime > COOLDOWN){
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccellerometer,
                    lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix, orientation);

            float azimuthInRadians = orientation[0];
            float azimuthInDegree = (float) Math.toDegrees(azimuthInRadians);

            RotateAnimation rotateAnimation =
                    new RotateAnimation(currentDegree, -azimuthInDegree, Animation.RELATIVE_TO_SELF,
                            0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(COOLDOWN);
            rotateAnimation.setFillAfter(true);
            compassImage.startAnimation(rotateAnimation);

            currentDegree = -azimuthInDegree;
            lastUpdatedTime = System.currentTimeMillis();

            int x = (int) azimuthInDegree;

            //TODO: add a text view for the "°" symbol
            degreesValueTextView.setText(x + "°");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do.
    }

    @Override
    public void onResume() {

        sensorManager.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);

        sensorManager.registerListener(this, magneticSensor,
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onPause() {

        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, magneticSensor);
    }

}
