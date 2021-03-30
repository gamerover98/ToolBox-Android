package it.uniba.magr.misurapp.navigation.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.realtime.NotConnectedException;
import it.uniba.magr.misurapp.database.realtime.RealtimeManager;
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeBarometer;
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeMagnetometer;
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeMeasure;
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeRuler;
import it.uniba.magr.misurapp.database.sqlite.SqliteManager;
import it.uniba.magr.misurapp.database.sqlite.bean.Barometer;
import it.uniba.magr.misurapp.database.sqlite.bean.Magnetometer;
import it.uniba.magr.misurapp.database.sqlite.bean.Measure;
import it.uniba.magr.misurapp.database.sqlite.bean.Ruler;
import it.uniba.magr.misurapp.database.sqlite.bean.Type;
import it.uniba.magr.misurapp.database.sqlite.dao.BarometersDao;
import it.uniba.magr.misurapp.database.sqlite.dao.MagnetometersDao;
import it.uniba.magr.misurapp.database.sqlite.dao.MeasurementsDao;
import it.uniba.magr.misurapp.database.sqlite.dao.RulersDao;
import it.uniba.magr.misurapp.navigation.Navigable;
import it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerAdapter;
import it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecycleTouchHelper;
import it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerGestureDetector;
import it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerGestureListener;
import lombok.Getter;

public class MainNavigation implements Navigable {

    /**
     * Gets the instance of the home activity.
     */
    @Getter
    private HomeActivity homeActivity;

    /**
     * The main progress bar view.
     */
    @SuppressWarnings("squid:S1450")
    private ProgressBar progressBar;

    /**
     * The text view that will be shown when there are no items.
     */
    @SuppressWarnings("squid:S1450")
    private MaterialTextView noItemsTextView;

    /**
     * The recycler view of the measurements.
     */
    private RecyclerView measurementsView;

    /**
     * The layout above the recycler view.
     */
    @SuppressWarnings("squid:S1450")
    private ViewGroup aboveLayout;

    /**
     * The main floating button that will be used to
     * navigate into the list tool's view.
     */
    private FloatingActionButton listToolFabButton;

    /**
     * The async thread that gets the items from the database.
     */
    private Thread obtainItemsThread;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.app_name);
    }

    @Override
    @SuppressWarnings("squid:S2696")
    public void onAttach(@NotNull Context context) {
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        progressBar       = activity.findViewById(R.id.main_progress_bar);
        noItemsTextView   = activity.findViewById(R.id.main_no_items_text_view);
        measurementsView  = activity.findViewById(R.id.main_measure_recycler_view);
        aboveLayout       = activity.findViewById(R.id.main_above_layout);
        listToolFabButton = activity.findViewById(R.id.main_list_tool_fab_button);

        initRecyclerView();
        initListToolFabButton();

    }

    @Override
    public void onStart() {

        if (obtainItemsThread != null) {
            obtainItemsThread.interrupt();
        }

        obtainItemsThread = new Thread(this :: obtainItemsFromDatabase);
        obtainItemsThread.start();

    }

    @Override
    public void onStop() {

        if (obtainItemsThread != null) {
            obtainItemsThread.interrupt();
        }

    }

    private void initRecyclerView() {

        MeasureRecyclerAdapter adapter = new MeasureRecyclerAdapter(measurementsView);
        GestureDetectorCompat recyclerGestureDetector = new GestureDetectorCompat(
                homeActivity, new MeasureRecyclerGestureDetector(measurementsView));
        MeasureRecyclerGestureListener gestureListener =
                new MeasureRecyclerGestureListener(recyclerGestureDetector);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity);

        measurementsView.setAdapter            (adapter);
        measurementsView.setLayoutManager      (linearLayoutManager);
        measurementsView.addOnItemTouchListener(gestureListener);
        measurementsView.setHasFixedSize       (true);

        // drag, right and left swipes.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MeasureRecycleTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(measurementsView);

        DrawerLayout drawerLayout = homeActivity.getDrawerLayout();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    private void initListToolFabButton() {
        listToolFabButton.setOnClickListener(this :: performAddMeasureClick);
    }

    private void performAddMeasureClick(@NotNull View view) {

        homeActivity.getNavController().navigate(R.id.nav_list_tools_fragment);
        homeActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    /**
     * Async method that will be executed when the application is resumed.
     * This method will gets the items from the database and adds it to the
     * recycler view.
     */
    private void obtainItemsFromDatabase() {

        SqliteManager sqliteManager = homeActivity.getSqliteManager();

        MeasurementsDao measurementsDao = sqliteManager.measurementsDao();
        List<Measure> localMeasureList = measurementsDao.getAll();

        checkLocalDatabaseMeasurements(localMeasureList);
        checkRemoteDatabaseMeasurements(localMeasureList);

        if (localMeasureList.isEmpty()) {

            homeActivity.runOnUiThread(() -> {

                progressBar.setVisibility(View.GONE);

                aboveLayout.setVisibility(View.VISIBLE);
                noItemsTextView.setVisibility(View.VISIBLE);

            });

        } else {

            homeActivity.runOnUiThread(() -> {

                progressBar.setVisibility(View.GONE);
                aboveLayout.setVisibility(View.VISIBLE);
                measurementsView.setVisibility(View.VISIBLE);
                noItemsTextView.setVisibility(View.GONE);

                MeasureRecyclerAdapter adapter = (MeasureRecyclerAdapter) measurementsView.getAdapter();
                assert adapter != null;

                adapter.clear();
                adapter.updateAll();

                int count = 0;

                for (int i = 0; i < localMeasureList.size() ; i++) {

                    Measure measure = localMeasureList.get(count);

                    if (!measure.isDeleted()) {

                        Type type = measure.getType();
                        int iconId;

                        switch (type) {

                            case RULER:        iconId = R.drawable.icon_ruler;        break;
                            case MAGNETOMETER: iconId = R.drawable.icon_magnetometer; break;
                            case LUX_METER:    iconId = R.drawable.icon_lux_meter;    break;
                            case BAROMETER:    iconId = R.drawable.icon_barometer;    break;
                            case PEDOMETER:    iconId = R.drawable.icon_pedometer;    break;
                            default:
                            case UNKNOWN: continue;
                        }

                        adapter.addMeasureEntry(iconId, measure);
                        adapter.update(count);
                        count++;

                    }

                }

            });

        }

    }

    /**
     * Check the sqlite database to obtain/update/remove items from the
     * remote firebase database.
     *
     * @param localMeasureList The not null list of local measurements.
     */
    @SuppressWarnings("squid:S3776")
    private void checkLocalDatabaseMeasurements(@NotNull List<Measure> localMeasureList) {

        SqliteManager sqliteManager = homeActivity.getSqliteManager();
        RealtimeManager realtimeManager = homeActivity.getRealtimeManager();
        MeasurementsDao measurementsDao = sqliteManager.measurementsDao();

        for (int i = 0 ; i < localMeasureList.size() ; i++) {

            Measure measure = localMeasureList.get(i);
            int measureId = measure.getId();
            Type type = measure.getType();

            if (!measure.isFirebaseSync()) {

                if (measure.isDeleted()) {

                    try {

                        realtimeManager.removeMeasure(measureId);

                        measure.setDeleted(true);
                        measurementsDao.removeMeasure(measure);

                    } catch (NotConnectedException notConnectedEx) {
                        Log.d(HomeActivity.HOME_LOG_TAG, "delete", notConnectedEx);
                    } catch (IllegalStateException isEx) {
                        Log.d(HomeActivity.HOME_LOG_TAG, "delete: " + isEx.getMessage());
                    }

                } else {

                    String title       = measure.getTitle();
                    String description = measure.getDescription();
                    Date   startDate   = measure.getStartDate();

                    try {

                        boolean hasMeasure = realtimeManager.hasMeasure(measureId);

                        if (hasMeasure) {
                            realtimeManager.updateMeasure(type, measureId, title, description);
                        } else {

                            int newMeasureId = realtimeManager.getMaxMeasureId() + 1;

                            if (type == Type.RULER) {

                                RulersDao rulersDao = sqliteManager.rulersDao();
                                Ruler ruler = rulersDao.getRuler(measureId);
                                RealtimeRuler realtimeRuler = new RealtimeRuler();

                                realtimeRuler.setMeasureId(newMeasureId);
                                realtimeRuler.setTitle(title);
                                realtimeRuler.setDescription(description);
                                realtimeRuler.setStartDate(startDate);
                                realtimeRuler.setLength(ruler.getLength());

                                realtimeManager.addMeasure(realtimeRuler);

                            } else if (type == Type.MAGNETOMETER) {

                                MagnetometersDao magnetometersDao = sqliteManager.magnetometersDao();
                                List<Magnetometer> magnetometers = magnetometersDao.getMagnetometers(measureId);
                                int length = magnetometers.size();

                                List<Integer> seconds = new ArrayList<>(length);
                                List<Float>   values  = new ArrayList<>(length);

                                for (int k = 0; k < length ; k++) {

                                    Magnetometer magnetometer = magnetometers.get(k);

                                    seconds.add(magnetometer.getTime());
                                    values.add((float) magnetometer.getValue());

                                }

                                RealtimeMagnetometer realtimeMagnetometer = new RealtimeMagnetometer();

                                realtimeMagnetometer.setMeasureId(newMeasureId);
                                realtimeMagnetometer.setTitle(title);
                                realtimeMagnetometer.setDescription(description);
                                realtimeMagnetometer.setStartDate(startDate);

                                realtimeMagnetometer.setSeconds(seconds);
                                realtimeMagnetometer.setValues(values);

                                realtimeManager.addMeasure(realtimeMagnetometer);

                            } else if (type == Type.BAROMETER) {

                                BarometersDao barometersDao = sqliteManager.barometersDao();
                                Barometer barometer = barometersDao.getBarometer(measureId);
                                RealtimeBarometer realtimeBarometer = new RealtimeBarometer();

                                realtimeBarometer.setMeasureId(newMeasureId);
                                realtimeBarometer.setTitle(title);
                                realtimeBarometer.setDescription(description);
                                realtimeBarometer.setStartDate(startDate);
                                realtimeBarometer.setPressure(barometer.getPressure());

                                realtimeManager.addMeasure(realtimeBarometer);

                            }

                        }

                        measure.setFirebaseSync(true);
                        measurementsDao.updateMeasure(measure);

                    } catch (NotConnectedException notConnectedEx) {
                        Log.d(HomeActivity.HOME_LOG_TAG, "update", notConnectedEx);
                    }

                }

            }

        }

    }

    /**
     * Check the remote firebase database to obtain/update/remove items to the
     * local sqlite database.
     *
     * @param localMeasureList The not null list of local measurements.
     */
    @SuppressWarnings("squid:S3776")
    private void checkRemoteDatabaseMeasurements(@NotNull List<Measure> localMeasureList) {

        SqliteManager sqliteManager = homeActivity.getSqliteManager();
        RealtimeManager realtimeManager = homeActivity.getRealtimeManager();
        MeasurementsDao measurementsDao = sqliteManager.measurementsDao();

        List<RealtimeMeasure> realtimeMeasurements = new ArrayList<>();
        int maxMeasureId;
        int remoteMeasureId;

        try {

            realtimeMeasurements.addAll(realtimeManager.getRulers());
            realtimeMeasurements.addAll(realtimeManager.getMagnetometers());
            realtimeMeasurements.addAll(realtimeManager.getBarometers());

            int latestRemoteMeasureId = realtimeManager.getMaxMeasureId();
            int latestLocalMeasureId = measurementsDao.getLatestMeasureID();

            maxMeasureId = Math.max(latestRemoteMeasureId, latestLocalMeasureId);

        } catch (NotConnectedException notConnectedEx) {
            return;
        }

        for (RealtimeMeasure remoteMeasure : realtimeMeasurements) {

            remoteMeasureId = remoteMeasure.getMeasureId();
            boolean isSavedLocally = false;

            for (Measure localMeasure : localMeasureList) {

                int localMeasureId = localMeasure.getId();

                if (remoteMeasureId == localMeasureId) {

                    isSavedLocally = true;
                    break;

                }

            }

            if (!isSavedLocally) {

                String title       = remoteMeasure.getTitle();
                String description = remoteMeasure.getDescription();
                Date   startDate   = remoteMeasure.getStartDate();
                Type   type        = Type.UNKNOWN;

                if (remoteMeasure instanceof RealtimeRuler) {
                    type = Type.RULER;
                } else if (remoteMeasure instanceof RealtimeBarometer) {
                    type = Type.BAROMETER;
                } else if (remoteMeasure instanceof RealtimeMagnetometer) {
                    type = Type.MAGNETOMETER;
                }

                int updatedMeasureId = ++maxMeasureId;

                try {

                    realtimeManager.updateMeasureId(remoteMeasureId, updatedMeasureId);

                } catch (NotConnectedException notConnectedEx) {
                    continue;
                }

                Measure measure = new Measure();

                measure.setId(updatedMeasureId);
                measure.setType(type);
                measure.setTitle(title);
                measure.setDescription(description);
                measure.setStartDate(startDate);
                measure.setFirebaseSync(true);
                measure.setDeleted(false);

                localMeasureList.add(measure);
                measurementsDao.insertMeasurements(measure);

                if (type == Type.RULER) {

                    RulersDao rulersDao = sqliteManager.rulersDao();
                    RealtimeRuler realtimeRuler = (RealtimeRuler) remoteMeasure;
                    double length = realtimeRuler.getLength();

                    Ruler ruler = new Ruler();

                    ruler.setMeasureId(measure.getId());
                    ruler.setLength(length);

                    rulersDao.insertRuler(ruler);

                } else if (type == Type.MAGNETOMETER) {

                    MagnetometersDao magnetometersDao = sqliteManager.magnetometersDao();
                    RealtimeMagnetometer realtimeMagnetometer = (RealtimeMagnetometer) remoteMeasure;

                    Integer[] seconds = realtimeMagnetometer.getSeconds().toArray(new Integer[0]);
                    Float[]   values  = realtimeMagnetometer.getValues().toArray(new Float[0]);
                    assert seconds.length == values.length;

                    int length = seconds.length;
                    Magnetometer[] magnetometers = new Magnetometer[length];

                    for (int i = 0 ; i < length ; i++) {

                        Magnetometer magnetometer = new Magnetometer();

                        magnetometer.setMeasureId(measure.getId());
                        magnetometer.setTime(seconds[i]);
                        magnetometer.setValue(values[i]);

                        magnetometers[i] = magnetometer;

                    }

                    magnetometersDao.insertMagnetometers(magnetometers);

                } else if (type == Type.BAROMETER) {

                    BarometersDao barometersDao = sqliteManager.barometersDao();
                    RealtimeBarometer realtimeBarometer = (RealtimeBarometer) remoteMeasure;
                    double pressure = realtimeBarometer.getPressure();

                    Barometer barometer = new Barometer();

                    barometer.setMeasureId(measure.getId());
                    barometer.setPressure(pressure);

                    barometersDao.insertBarometer(barometer);

                }

            }

        }

    }

}
