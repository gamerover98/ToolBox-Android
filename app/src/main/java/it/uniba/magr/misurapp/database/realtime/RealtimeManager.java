package it.uniba.magr.misurapp.database.realtime;

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

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeMeasure;
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeRuler;
import it.uniba.magr.misurapp.database.sqlite.bean.Type;

public class RealtimeManager {

    private static final GenericTypeIndicator<List<RealtimeRuler>> RULER_TYPE_INDICATOR
            = new GenericTypeIndicator<List<RealtimeRuler>>() {};

    private static final String CHILD_USERS        = "users";
    private static final String CHILD_MEASUREMENTS = "measurements";
    private static final String CHILD_RULERS       = "rulers";

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

    public void addRuler(@NotNull RealtimeRuler ruler) throws NotConnectedException {

        String uuid = getUserUUID();

        if (uuid == null) {
            return;
        }

        List<RealtimeRuler> existingRulers = new ArrayList<>(getRulers());
        existingRulers.add(ruler);

        DatabaseReference databaseReference = getMeasureChild(uuid);

        databaseReference = databaseReference.child(CHILD_RULERS);
        databaseReference.setValue(existingRulers);
        databaseReference.push();

    }

    public void removeRuler(int measureId) throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return;
        }

        List<RealtimeRuler> existingRulers = new ArrayList<>(getRulers());

        for (int i = 0 ; i < existingRulers.size() ; i++) {

            RealtimeRuler current = existingRulers.get(i);
            int currentId = current.getMeasureId();

            if (measureId == currentId) {

                existingRulers.remove(i);
                break;

            }

        }

        DatabaseReference databaseReference = getMeasureChild(uuid);

        databaseReference = databaseReference.child(CHILD_RULERS);
        databaseReference.setValue(existingRulers);
        databaseReference.push();

    }

    public boolean hasMeasure(int measureId) throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return false;
        }

        List<RealtimeMeasure> existingMeasurements = getRealtimeMeasures();

        for (RealtimeMeasure realtimeMeasure : existingMeasurements) {

            int currentId = realtimeMeasure.getMeasureId();

            if (measureId == currentId) {
                return true;
            }

        }

        return false;

    }

    public void updateMeasureId(int remoteMeasureId, int updatedMeasureId) throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return;
        }

        List<RealtimeMeasure> existingMeasurements = getRealtimeMeasures();
        Type currentType = Type.UNKNOWN;

        for (int i = 0 ; i < existingMeasurements.size() ; i++) {

            RealtimeMeasure current = existingMeasurements.get(i);
            int currentId = current.getMeasureId();

            if (remoteMeasureId == currentId) {

                current.setMeasureId(updatedMeasureId);

                if (current instanceof RealtimeRuler) {
                    currentType = Type.RULER;
                }
                //TODO: add magnetometer and barometer

                break;

            }

        }

        DatabaseReference databaseReference = getMeasureChild(uuid);

        switch (currentType) {
            case RULER:

                databaseReference = databaseReference.child(CHILD_RULERS);
                databaseReference.setValue(existingMeasurements);

                break;

            case MAGNETOMETER: /* needs to be implemented */ break;
            case BAROMETER:    /* needs to be implemented */ break;
            default: return;
        }

        databaseReference.push();

    }

    public void updateMeasure(Type type, int measureId,
                              @NotNull String title,
                              @NotNull String description) throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return;
        }

        List<RealtimeMeasure> existingMeasurements = getRealtimeMeasures(type);

        for (int i = 0 ; i < existingMeasurements.size() ; i++) {

            RealtimeMeasure current = existingMeasurements.get(i);
            int currentId = current.getMeasureId();

            if (measureId == currentId) {

                current.setTitle(title);
                current.setDescription(description);
                break;

            }

        }

        DatabaseReference databaseReference = getMeasureChild(uuid);

        switch (type) {
            case RULER:

                databaseReference = databaseReference.child(CHILD_RULERS);
                databaseReference.setValue(existingMeasurements);

                break;

            case MAGNETOMETER: /* needs to be implemented */ break;
            case BAROMETER:    /* needs to be implemented */ break;
            default: return;
        }

        databaseReference.push();

    }

    //
    // GETTERS
    //

    @NotNull
    private List<RealtimeMeasure> getRealtimeMeasures(@NotNull Type type) throws NotConnectedException {

        List<RealtimeMeasure> result = new ArrayList<>();

        switch (type) {
            case RULER:        result.addAll(getRulers()); break;
            case MAGNETOMETER: /* needs to be implemented */ break;
            case BAROMETER:    /* needs to be implemented */ break;
            default: break;
        }

        return result;

    }

    @NotNull
    private List<RealtimeMeasure> getRealtimeMeasures() throws NotConnectedException {

        List<RealtimeMeasure> result = new ArrayList<>();

        result.addAll(getRulers());
        //TODO: magnetometer and barometer

        return result;

    }

    public int getMaxMeasureId() throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        int max = 0;

        for (RealtimeRuler realtimeRuler : getRulers()) {

            int measureId = realtimeRuler.getMeasureId();

            if (measureId > max) {
                max = measureId;
            }

        }

        return 0;

    }

    @NotNull
    public List<RealtimeRuler> getRulers() throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        String uuid = getUserUUID();

        if (uuid == null) {
            return Collections.unmodifiableList(new ArrayList<>());
        }

        DatabaseReference databaseReference = getMeasureChild(uuid);
        databaseReference = databaseReference.child(CHILD_RULERS);

        return getRulers(databaseReference);

    }

    @NotNull
    @SuppressWarnings("squid:S3776")
    public List<RealtimeRuler> getRulers(@NotNull DatabaseReference databaseReference)
            throws NotConnectedException {

        if (!isNetworkConnected()) {
            throw new NotConnectedException();
        }

        List<RealtimeRuler> results = new ArrayList<>();
        Task<DataSnapshot> dataSnapshotTask = databaseReference.get();

        dataSnapshotTask.addOnCanceledListener(this :: unlock);
        dataSnapshotTask.addOnFailureListener(onFailure -> unlock());

        dataSnapshotTask.addOnSuccessListener(dataSnapshot -> {

            if (dataSnapshot != null) {

                List<RealtimeRuler> rulers = dataSnapshot.getValue(RULER_TYPE_INDICATOR);

                if (rulers != null && !rulers.isEmpty()) {

                    for (RealtimeRuler current : rulers) {

                        if (current != null) {
                            results.add(current);
                        }

                    }

                }

            }

            unlock();

        });

        lock();
        return Collections.unmodifiableList(results);

    }

    /**
     * @return True if the device is connected to an internet connection.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isNetworkConnected() {

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    //
    // PRIVATE METHODS
    //

    @NotNull
    private DatabaseReference getMeasureChild(@NotNull String uuid) {

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