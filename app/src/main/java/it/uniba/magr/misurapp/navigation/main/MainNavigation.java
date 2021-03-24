package it.uniba.magr.misurapp.navigation.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import java.util.List;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.DatabaseManager;
import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.bean.Type;
import it.uniba.magr.misurapp.database.dao.MeasurementsDao;
import it.uniba.magr.misurapp.navigation.Navigable;
import it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerAdapter;
import it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecycleTouchHelper;
import it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerGestureDetector;
import it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerGestureListener;
import lombok.Getter;

public class MainNavigation implements Navigable {

    /**
     * Gets a static access to this instance.
     */
    @Getter
    @SuppressWarnings("StaticFieldLeak")
    private static MainNavigation instance;

    /**
     * Gets the instance of the home activity.
     */
    @Getter
    private HomeActivity homeActivity;

    /**
     * The main progress bar view.
     */
    private ProgressBar progressBar;

    /**
     * The text view that will be shown when there are no items.
     */
    private MaterialTextView noItemsTextView;

    /**
     * The recycler view of the measurements.
     */
    private RecyclerView measurementsView;

    /**
     * The layout above the recycler view.
     */
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

        instance = this;
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
    public void onResume() {

        if (obtainItemsThread != null) {
            obtainItemsThread.interrupt();
        }

        obtainItemsThread = new Thread(this :: obtainItemsFromDatabase);
        obtainItemsThread.start();

    }

    @Override
    @SuppressWarnings("squid:S2696")
    public void onDestroyView() {
        instance = null;
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

        DatabaseManager databaseManager = homeActivity.getDatabaseManager();
        MeasurementsDao measurementsDao = databaseManager.measurementsDao();

        List<Measure> measureList = measurementsDao.getAll();

        if (measureList.isEmpty()) {

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

                for (int i = 0 ; i < measureList.size() ; i++) {

                    Measure measure = measureList.get(i);
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
                    adapter.update(i);

                }

            });

        }

    }


}
