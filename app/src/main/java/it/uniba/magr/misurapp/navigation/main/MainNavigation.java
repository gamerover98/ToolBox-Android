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
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeMeasure;
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeRuler;
import it.uniba.magr.misurapp.database.sqlite.SqliteManager;
import it.uniba.magr.misurapp.database.sqlite.bean.Measure;
import it.uniba.magr.misurapp.database.sqlite.bean.Ruler;
import it.uniba.magr.misurapp.database.sqlite.bean.Type;
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
        RulersDao rulersDao = sqliteManager.rulersDao();

        for (int i = 0; i < localMeasureList.size() ; i++) {

            Measure measure = localMeasureList.get(i);
            int measureId = measure.getId();
            Type type = measure.getType();

            if (!measure.isFirebaseSync()) {

                if (measure.isDeleted()) {

                    try {

                        switch (type) {
                            case RULER:        realtimeManager.removeRuler(measureId); break;
                            case BAROMETER:    /* needs to be implemented */           break;
                            case MAGNETOMETER: /* needs to be implemented */           break;
                            case UNKNOWN: throw new IllegalStateException("Unknown measure");
                            default: break;
                        }

                        measure.setDeleted(true);
                        measurementsDao.removeMeasure(measure);

                    } catch (NotConnectedException notConnectedEx) {
                        Log.d(HomeActivity.HOME_LOG_TAG, "delete", notConnectedEx);
                    } catch (IllegalStateException isEx) {
                        Log.d(HomeActivity.HOME_LOG_TAG, isEx.getMessage());
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

                                Ruler ruler = rulersDao.getRuler(measureId);
                                RealtimeRuler realtimeRuler = new RealtimeRuler();

                                realtimeRuler.setMeasureId(newMeasureId);
                                realtimeRuler.setTitle(title);
                                realtimeRuler.setDescription(description);
                                realtimeRuler.setStartDate(startDate);
                                realtimeRuler.setLength(ruler.getLength());

                                realtimeManager.addRuler(realtimeRuler);

                            }
                            //TODO: magnetometer and barometer

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
        RulersDao rulersDao = sqliteManager.rulersDao();

        List<RealtimeMeasure> realtimeMeasurements = new ArrayList<>();
        int maxMeasureId;
        int remoteMeasureId;

        try {

            realtimeMeasurements.addAll(realtimeManager.getRulers());
            //TODO: magnetometer
            //TODO: barometer

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
                }
                //TODO: magnetometer and barometer

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

                    RealtimeRuler realtimeRuler = (RealtimeRuler) remoteMeasure;
                    double length = realtimeRuler.getLength();

                    Ruler ruler = new Ruler();

                    ruler.setMeasureId(measure.getId());
                    ruler.setLength(length);

                    rulersDao.insertRuler(ruler);

                }
                //TODO: magnetometer and barometer

            }

        }

    }

}
