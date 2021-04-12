package it.uniba.magr.toolbox.navigation.main.recycle;

import android.content.Context;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import it.uniba.magr.toolbox.HomeActivity;
import it.uniba.magr.toolbox.R;
import it.uniba.magr.toolbox.database.sqlite.SqliteManager;
import it.uniba.magr.toolbox.database.sqlite.bean.Barometer;
import it.uniba.magr.toolbox.database.sqlite.bean.Magnetometer;
import it.uniba.magr.toolbox.database.sqlite.bean.Measure;
import it.uniba.magr.toolbox.database.sqlite.bean.Ruler;
import it.uniba.magr.toolbox.database.sqlite.bean.Type;
import it.uniba.magr.toolbox.database.sqlite.bean.embedded.MeasureAndBarometer;
import it.uniba.magr.toolbox.database.sqlite.bean.embedded.MeasureAndRuler;
import it.uniba.magr.toolbox.database.sqlite.dao.MagnetometersDao;
import it.uniba.magr.toolbox.database.sqlite.dao.MeasurementsDao;
import it.uniba.magr.toolbox.navigation.main.entry.MeasureEntry;
import it.uniba.magr.toolbox.tool.magnetometer.MagnetometerNavigation;
import it.uniba.magr.toolbox.tool.ruler.RulerNavigation;

import static it.uniba.magr.toolbox.tool.barometer.BarometerNavigation.BUNDLE_PRESSURE_KEY;

public class MeasureRecyclerGestureDetector extends GestureDetector.SimpleOnGestureListener {

    public static final String BUNDLE_MEASURE_ID_KEY  = "measure_id";
    public static final String BUNDLE_TITLE_KEY       = "title";
    public static final String BUNDLE_DESCRIPTION_KEY = "description";

    private final RecyclerView recyclerView;

    public MeasureRecyclerGestureDetector(@NotNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return onContextClick(motionEvent);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return onContextClick(motionEvent);
    }

    @Override
    public boolean onContextClick(MotionEvent motionEvent) {

        View itemView = getItem(motionEvent);

        if (itemView != null) {

            MeasureRecyclerAdapter adapter = (MeasureRecyclerAdapter) recyclerView.getAdapter();
            assert adapter != null;

            int position = getItemPosition(itemView);
            MeasureEntry measureEntry = adapter.getMeasureEntry(position);

            if (measureEntry != null) {

                Measure measure = measureEntry.getMeasure();
                Type type = measure.getType();

                Thread thread = new Thread(() -> {

                    switch (type)  {
                        case RULER:        openRuleSaving(measure);         break;
                        case MAGNETOMETER: openMagnetometerSaving(measure); break;
                        case BAROMETER:    openBarometerSaving(measure);    break;
                        default: break;
                    }

                });

                thread.start();

            }

        }

        return super.onContextClick(motionEvent);

    }

    private void openRuleSaving(@NotNull Measure measure) {

        int measureId = measure.getId();
        String title = measure.getTitle();
        String description = measure.getDescription();

        Context context = recyclerView.getContext();
        HomeActivity activity = (HomeActivity) context;

        SqliteManager sqliteManager = activity.getSqliteManager();
        MeasurementsDao measurementsDao = sqliteManager.measurementsDao();

        Optional<MeasureAndRuler> opt = measurementsDao.getRulerMeasure(measureId).stream().findAny();

        if (!opt.isPresent()) {
            return;
        }

        MeasureAndRuler measureAndRuler = opt.get();
        Ruler ruler = measureAndRuler.getRuler();

        float centimeters = (float) ruler.getLength();

        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_MEASURE_ID_KEY,     measureId);
        bundle.putString(BUNDLE_TITLE_KEY,       title);
        bundle.putString(BUNDLE_DESCRIPTION_KEY, description);

        bundle.putFloat(RulerNavigation.BUNDLE_LENGTH_KEY, centimeters);

        activity.runOnUiThread(() -> {

            NavController navController = activity.getNavController();
            navController.navigate(R.id.action_nav_edit_measure_fragment_to_ruler, bundle);

        });

    }

    private void openBarometerSaving(@NotNull Measure measure) {

        int measureId = measure.getId();
        String title = measure.getTitle();
        String description = measure.getDescription();

        Context context = recyclerView.getContext();
        HomeActivity activity = (HomeActivity) context;

        SqliteManager sqliteManager = activity.getSqliteManager();
        MeasurementsDao measurementsDao = sqliteManager.measurementsDao();

        Optional<MeasureAndBarometer> opt = measurementsDao.getBarometerMeasure(measureId).stream().findAny();

        if (!opt.isPresent()) {
            return;
        }

        MeasureAndBarometer measureAndBarometer = opt.get();
        Barometer barometer =  measureAndBarometer.getBarometer();
        float pressure = (float) barometer.getPressure();

        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_MEASURE_ID_KEY,     measureId);
        bundle.putString(BUNDLE_TITLE_KEY,       title);
        bundle.putString(BUNDLE_DESCRIPTION_KEY, description);

        bundle.putFloat(BUNDLE_PRESSURE_KEY, pressure);

        activity.runOnUiThread(() -> {

            NavController navController = activity.getNavController();
            navController.navigate(R.id.action_nav_edit_measure_fragment_to_barometer, bundle);

        });

    }


    private void openMagnetometerSaving(@NotNull Measure measure) {

        int measureId = measure.getId();
        String title = measure.getTitle();
        String description = measure.getDescription();

        Context context = recyclerView.getContext();
        HomeActivity activity = (HomeActivity) context;

        SqliteManager sqliteManager = activity.getSqliteManager();
        MagnetometersDao magnetometersDao = sqliteManager.magnetometersDao();

        List<Magnetometer> results = magnetometersDao.getMagnetometers(measureId);

        int length = results.size();
        int[] seconds = new int[length];
        float[] values = new float[length];

        for (int i = 0; i < length; i++) {

            Magnetometer magnetometer = results.get(i);

            seconds[i] = magnetometer.getTime();
            values[i] = (float) magnetometer.getValue();

        }

        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_MEASURE_ID_KEY,     measureId);
        bundle.putString(BUNDLE_TITLE_KEY,       title);
        bundle.putString(BUNDLE_DESCRIPTION_KEY, description);

        bundle.putIntArray(MagnetometerNavigation.BUNDLE_SECONDS_KEY, seconds);
        bundle.putFloatArray(MagnetometerNavigation.BUNDLE_VALUES_KEY, values);

        activity.runOnUiThread(() -> {

            NavController navController = activity.getNavController();
            navController.navigate(R.id.action_nav_edit_measure_fragment_to_magnetometer, bundle);

        });

    }

    @Nullable
    private View getItem(@NotNull MotionEvent motionEvent) {

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        return recyclerView.findChildViewUnder(x, y);

    }

    private int getItemPosition(@NotNull View itemView) {
        return recyclerView.getChildAdapterPosition(itemView);
    }

}
