package it.uniba.magr.toolbox.database.realtime;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import it.uniba.magr.toolbox.HomeActivity;
import it.uniba.magr.toolbox.database.realtime.bean.RealtimeBarometer;
import it.uniba.magr.toolbox.database.realtime.bean.RealtimeMagnetometer;
import it.uniba.magr.toolbox.database.realtime.bean.RealtimeMeasure;
import it.uniba.magr.toolbox.database.realtime.bean.RealtimeRuler;
import it.uniba.magr.toolbox.database.sqlite.bean.Type;

public class RealtimeManager {

    private static final GenericTypeIndicator<List<RealtimeRuler>> RULER_TYPE_INDICATOR
            = new GenericTypeIndicator<List<RealtimeRuler>>() {};

    private static final GenericTypeIndicator<List<RealtimeBarometer>> BAROMETER_TYPE_INDICATOR
            = new GenericTypeIndicator<List<RealtimeBarometer>>() {};

    private static final GenericTypeIndicator<List<RealtimeMagnetometer>> MAGNETOMETER_TYPE_INDICATOR
            = new GenericTypeIndicator<List<RealtimeMagnetometer>>() {};

    private static final String CHILD_USERS         = "users";
    private static final String CHILD_MEASUREMENTS  = "measurements";
    private static final String CHILD_RULERS        = "rulers";
    private static final String CHILD_MAGNETOMETERS = "magnetometers";
    private static final String CHILD_BAROMETERS    = "barometers";

    /**
     * This field prevents the code execution from flowing until the
     * async operations are ended.
     */
    private final AtomicBoolean lock = new AtomicBoolean(true);

    /**
     * The not null connectivity manager instance.
     */
    @NotNull
    private final ConnectivityManager connectivityManager;

    public RealtimeManager(@NotNull HomeActivity homeActivity) {

        this.connectivityManager = (ConnectivityManager)
                homeActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;

    }

    //
    // PUBLIC METHODS
    //

    /**
     * @return True if the device is connected to an internet connection.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isNetworkConnected() {

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    /**
     * Add a ruler to the realtime database.
     *
     * @param measure The realtime ruler instance.
     * @throws NotConnectedException invoked if the device is not connected to internet.
     */
    @SuppressWarnings("unchecked")
    public void addMeasure(@NotNull RealtimeMeasure measure) throws NotConnectedException {

        String uuid = getUserUUID();

        if (uuid == null) {
            return;
        }

        Type currentType = Type.UNKNOWN;

        if (measure instanceof RealtimeRuler) {
            currentType = Type.RULER;
        } else if (measure instanceof RealtimeBarometer) {
            currentType = Type.BAROMETER;
        } else if (measure instanceof RealtimeMagnetometer) {
            currentType = Type.MAGNETOMETER;
        }

        if (currentType == Type.UNKNOWN) {
            return;
        }

        List<RealtimeMeasure> measurements = (List<RealtimeMeasure>) getSpecificMeasurements(currentType, uuid);
        measurements.add(measure);

        DatabaseReference databaseReference;

        switch (currentType) {

            case RULER:        databaseReference = getRulersChild(uuid);        break;
            case MAGNETOMETER: databaseReference = getMagnetometersChild(uuid); break;
            case BAROMETER:    databaseReference = getBarometersChild(uuid);    break;
            default: return;

        }

        databaseReference.setValue(measurements);
        databaseReference.push();

    }

    /**
     * Remove a measure from the realtime database.
     *
     * @param measureIdToRemove The measure id to remove.
     * @throws NotConnectedException invoked if the device is not connected to internet.
     */
    public void removeMeasure(int measureIdToRemove) throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return;
        }

        List<? extends RealtimeMeasure> measurements = getAllMeasurements(uuid);
        Type currentType = Type.UNKNOWN;

        for (RealtimeMeasure current : measurements) {

            if (measureIdToRemove == current.getMeasureId()) {

                if (current instanceof RealtimeRuler) {
                    currentType = Type.RULER;
                } else if (current instanceof RealtimeBarometer) {
                    currentType = Type.BAROMETER;
                } else if (current instanceof RealtimeMagnetometer) {
                    currentType = Type.MAGNETOMETER;
                }

                break;

            }

        }

        if (currentType == Type.UNKNOWN) {
            return;
        }

        measurements = getSpecificMeasurements(currentType, uuid);
        RealtimeMeasure toRemoveMeasure = null;

        for (RealtimeMeasure current : measurements) {

            if (measureIdToRemove == current.getMeasureId()) {

                toRemoveMeasure = current;
                break;

            }

        }

        measurements.remove(toRemoveMeasure);
        DatabaseReference databaseReference;

        switch (currentType) {

            case RULER:        databaseReference = getRulersChild(uuid);        break;
            case MAGNETOMETER: databaseReference = getMagnetometersChild(uuid); break;
            case BAROMETER:    databaseReference = getBarometersChild(uuid);    break;
            default: return;

        }

        databaseReference.setValue(measurements);
        databaseReference.push();

    }

    /**
     * Check if a measure id is contained into the remote database.
     *
     * @param measureId The measure id.
     * @throws NotConnectedException invoked if the device is not connected to internet.
     */
    public boolean hasMeasure(int measureId) throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return false;
        }

        List<RealtimeMeasure> allMeasurements = getAllMeasurements(uuid);

        for (RealtimeMeasure currentMeasure : allMeasurements) {

            int currentId = currentMeasure.getMeasureId();

            if (measureId == currentId) {
                return true;
            }

        }

        return false;

    }

    /**
     * Update the measure id of an existing measure.
     *
     * @param remoteMeasureId The remote measure id to edit.
     * @param updatedMeasureId The replacement measure id.
     * @throws NotConnectedException invoked if the device is not connected to internet.
     */
    public void updateMeasureId(int remoteMeasureId, int updatedMeasureId) throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return;
        }

        List<? extends RealtimeMeasure> measurements = getAllMeasurements(uuid);
        Type currentType = Type.UNKNOWN;

        for (RealtimeMeasure current : measurements) {

            if (remoteMeasureId == current.getMeasureId()) {

                if (current instanceof RealtimeRuler) {
                    currentType = Type.RULER;
                } else if (current instanceof RealtimeBarometer) {
                    currentType = Type.BAROMETER;
                } else if (current instanceof RealtimeMagnetometer) {
                    currentType = Type.MAGNETOMETER;
                }

                break;

            }

        }

        if (currentType == Type.UNKNOWN) {
            return;
        }

        measurements = getSpecificMeasurements(currentType, uuid);

        for (RealtimeMeasure current : measurements) {

            if (remoteMeasureId == current.getMeasureId()) {
                current.setMeasureId(updatedMeasureId);
            }

        }

        DatabaseReference databaseReference;

        switch (currentType) {

            case RULER:        databaseReference = getRulersChild(uuid);        break;
            case MAGNETOMETER: databaseReference = getMagnetometersChild(uuid); break;
            case BAROMETER:    databaseReference = getBarometersChild(uuid);    break;
            default: return;

        }

        databaseReference.setValue(measurements);
        databaseReference.push();

    }

    /**
     * Update a measure title and description.
     *
     * @param type The not null Type of the measure.
     * @param measureId The unique measure id of the remote item.
     * @param title The not null title.
     * @param description The not null description.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @SuppressWarnings("unchecked")
    public void updateMeasure(@NotNull Type type,
                              int measureId,
                              @NotNull String title,
                              @NotNull String description) throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return;
        }

        List<RealtimeMeasure> measurements = (List<RealtimeMeasure>) getSpecificMeasurements(type, uuid);

        for (int i = 0; i < measurements.size() ; i++) {

            RealtimeMeasure current = measurements.get(i);
            int currentId = current.getMeasureId();

            if (measureId == currentId) {

                current.setTitle(title);
                current.setDescription(description);
                break;

            }

        }

        DatabaseReference databaseReference;

        switch (type) {

            case RULER:        databaseReference = getRulersChild(uuid);        break;
            case MAGNETOMETER: databaseReference = getMagnetometersChild(uuid); break;
            case BAROMETER:    databaseReference = getBarometersChild(uuid);    break;
            default: return;

        }

        databaseReference.setValue(measurements);
        databaseReference.push();

    }

    /**
     * @return The maximum measure id into the user remote database.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    public int getMaxMeasureId() throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return -1;
        }

        int max = 0;

        for (RealtimeMeasure current : getAllMeasurements(uuid)) {

            int measureId = current.getMeasureId();

            if (measureId > max) {
                max = measureId;
            }

        }

        return 0;

    }

    /**
     * Retrieve a list of ruler measurements from the remote database
     * associated with the current logged user.
     *
     * @return A not null list of ruler measurements.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    public List<RealtimeRuler> getRulers() throws NotConnectedException {

        String uuid = getUserUUID();

        if (uuid == null) {
            return new ArrayList<>();
        }

        return getRulers(uuid);

    }

    /**
     * Retrieve a list of magnetometer measurements from the remote database
     * associated with the current logged user.
     *
     * @return A not null list of magnetometer measurements.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    public List<RealtimeMagnetometer> getMagnetometers() throws NotConnectedException {

        String uuid = getUserUUID();

        if (uuid == null) {
            return new ArrayList<>();
        }

        return getMagnetometers(uuid);

    }

    /**
     * Retrieve a list of barometer measurements from the remote database
     * associated with the current logged user.
     *
     * @return A not null list of barometer measurements.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    public List<RealtimeBarometer> getBarometers() throws NotConnectedException {

        String uuid = getUserUUID();

        if (uuid == null) {
            return new ArrayList<>();
        }

        return getBarometers(uuid);

    }

    /**
     * Retrieve a list of ruler measurements from the remote database.
     *
     * @param uuid The not null user firebase uuid.
     * @return A not null list of ruler measurements.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    @SuppressWarnings({"squid:S3776", "unchecked"})
    public List<RealtimeRuler> getRulers(@NotNull String uuid) throws NotConnectedException {

        return (List<RealtimeRuler>) getMeasurements(getRulersChild(uuid), dataSnapshot -> {

            List<RealtimeRuler> result = new ArrayList<>();
            List<RealtimeRuler> rulers = dataSnapshot.getValue(RULER_TYPE_INDICATOR);

            if (rulers != null && !rulers.isEmpty()) {

                for (RealtimeRuler current : rulers) {

                    if (current != null) {
                        result.add(current);
                    }

                }

            }

            return result;

        });

    }

    /**
     * Retrieve a list of magnetometer measurements from the remote database.
     *
     * @param uuid The not null user firebase uuid.
     * @return A not null list of magnetometer measurements.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    @SuppressWarnings({"squid:S3776", "unchecked"})
    public List<RealtimeMagnetometer> getMagnetometers(@NotNull String uuid) throws NotConnectedException {

        return (List<RealtimeMagnetometer>) getMeasurements(getMagnetometersChild(uuid), dataSnapshot -> {

            List<RealtimeMagnetometer> result = new ArrayList<>();
            List<RealtimeMagnetometer> magnetometers = dataSnapshot.getValue(MAGNETOMETER_TYPE_INDICATOR);

            if (magnetometers != null && !magnetometers.isEmpty()) {

                for (RealtimeMagnetometer current : magnetometers) {

                    if (current != null) {
                        result.add(current);
                    }

                }

            }

            return result;

        });

    }

    /**
     * Retrieve a list of barometer measurements from the remote database.
     *
     * @param uuid The not null user firebase uuid.
     * @return A not null list of barometer measurements.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    @SuppressWarnings({"squid:S3776", "unchecked"})
    public List<RealtimeBarometer> getBarometers(@NotNull String uuid) throws NotConnectedException {

        return (List<RealtimeBarometer>) getMeasurements(getBarometersChild(uuid), dataSnapshot -> {

            List<RealtimeBarometer> result = new ArrayList<>();
            List<RealtimeBarometer> barometers = dataSnapshot.getValue(BAROMETER_TYPE_INDICATOR);

            if (barometers != null && !barometers.isEmpty()) {

                for (RealtimeBarometer current : barometers) {

                    if (current != null) {
                        result.add(current);
                    }

                }

            }

            return result;

        });

    }

    //
    // PRIVATE METHODS
    //

    /**
     * Retrieve a list of measurements from the remote database.
     *
     * @param childReference The not null measurements child reference instance.
     * @param dataSnapshotConsumer The not null lambda Function to gets the result.
     * @return A not null list of measurements.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    private List<? extends RealtimeMeasure> getMeasurements(
            @NotNull DatabaseReference childReference,
            @NotNull Function<DataSnapshot, List<? extends RealtimeMeasure>> dataSnapshotConsumer)
            throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        List<RealtimeMeasure> results = new ArrayList<>();
        Task<DataSnapshot> dataSnapshotTask = childReference.get();

        dataSnapshotTask.addOnCanceledListener(this :: unlock);
        dataSnapshotTask.addOnFailureListener(onFailure -> unlock());

        dataSnapshotTask.addOnSuccessListener(dataSnapshot -> {

            if (dataSnapshot != null) {
                results.addAll(dataSnapshotConsumer.apply(dataSnapshot));
            }

            unlock();

        });

        lock();
        return Collections.unmodifiableList(results);

    }

    /**
     * @param uuid The not null user firebase uuid.
     * @return A not null list of all measurements associate with the uuid.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    private List<RealtimeMeasure> getAllMeasurements(@NotNull String uuid) throws NotConnectedException {

        List<RealtimeMeasure> result = new ArrayList<>();

        result.addAll(getRulers(uuid));
        result.addAll(getMagnetometers(uuid));
        result.addAll(getBarometers(uuid));

        return result;

    }

    /**
     * @param type The not null measure type.
     * @param uuid The not null user firebase uuid.
     * @return A not null list of all measurements associate with the uuid.
     * @throws NotConnectedException Invoked if the device is not connected to internet.
     */
    @NotNull
    private List<? extends RealtimeMeasure> getSpecificMeasurements(@NotNull Type type,
                                                          @NotNull String uuid) throws NotConnectedException {

        List<RealtimeMeasure> result = new ArrayList<>();

        switch (type) {
            case RULER:        result.addAll(getRulers(uuid));        break;
            case MAGNETOMETER: result.addAll(getMagnetometers(uuid)); break;
            case BAROMETER:    result.addAll(getBarometers(uuid));    break;
            default: break;
        }

        return result;

    }

    /**
     * @param uuid The not null user firebase uuid.
     * @return a not null instance of the rulers child.
     */
    @NotNull
    private DatabaseReference getRulersChild(@NotNull String uuid) {

        DatabaseReference databaseReference = getMeasurementsChild(uuid);
        return databaseReference.child(CHILD_RULERS);

    }

    /**
     * @param uuid The not null user firebase uuid.
     * @return a not null instance of the magnetometers child.
     */
    @NotNull
    private DatabaseReference getMagnetometersChild(@NotNull String uuid) {

        DatabaseReference databaseReference = getMeasurementsChild(uuid);
        return databaseReference.child(CHILD_MAGNETOMETERS);

    }

    /**
     * @param uuid The not null user firebase uuid.
     * @return a not null instance of the barometers child.
     */
    @NotNull
    private DatabaseReference getBarometersChild(@NotNull String uuid) {

        DatabaseReference databaseReference = getMeasurementsChild(uuid);
        return databaseReference.child(CHILD_BAROMETERS);

    }

    @NotNull
    private DatabaseReference getMeasurementsChild(@NotNull String uuid) {

        DatabaseReference databaseReference = getUserChild(uuid);
        return databaseReference.child(CHILD_MEASUREMENTS);

    }

    @NotNull
    private DatabaseReference getUserChild(@NotNull String uuid) {

        DatabaseReference databaseReference = getDatabaseReference();
        databaseReference = databaseReference.child(CHILD_USERS);

        return databaseReference.child(uuid);

    }

    /**
     * @return The not null database reference instance.
     */
    @NotNull
    private DatabaseReference getDatabaseReference() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        return firebaseDatabase.getReference();

    }

    /**
     * Gets the authenticated user UUID.
     * @return The user UUID. Null if it is not logged.
     */
    @Nullable
    private String getUserUUID() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            return firebaseUser.getUid();
        }

        return null;

    }

    /**
     * Lock the current thread to grant the correct performing of async requests.
     * While these requests are not completed, this thread remains locked.
     */
    private void lock() {

        // synchronize to the current lock field to
        // grant the lock.
        synchronized (lock) {

            // while the loop during the field value is true.
            while (lock.get()) {

                try {

                    lock.wait(50L);

                } catch (InterruptedException iEx) {

                    lock.set(true);

                    Log.e("REALTIME-DB", "Thread locking interrupted", iEx);
                    Thread.currentThread().interrupt();
                    break;

                }

            }

            lock.set(true);

        }

    }

    /**
     * Unlock the current thread to grant the correct performing of async requests.
     */
    private void unlock() {

        synchronized (lock) {

            lock.set(false);
            lock.notifyAll();

        }

    }

}