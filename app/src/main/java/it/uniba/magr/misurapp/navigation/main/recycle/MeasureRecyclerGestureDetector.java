package it.uniba.magr.misurapp.navigation.main.recycle;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.DatabaseManager;
import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.bean.Ruler;
import it.uniba.magr.misurapp.database.bean.Type;
import it.uniba.magr.misurapp.database.bean.embedded.MeasureAndRuler;
import it.uniba.magr.misurapp.database.dao.MeasurementsDao;
import it.uniba.magr.misurapp.navigation.main.entry.MeasureEntry;
import it.uniba.magr.misurapp.tool.ruler.RulerNavigation;

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
                        case BAROMETER:    openBarometerSaving(measure);    break;
                        case MAGNETOMETER: openMagnetometerSaving(measure); break;
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

        DatabaseManager databaseManager = activity.getDatabaseManager();
        MeasurementsDao measurementsDao = databaseManager.measurementsDao();

        Optional<MeasureAndRuler> opt = measurementsDao.getRulerMeasure(measureId).stream().findAny();

        Log.d("TEST", "" + opt.isPresent());

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
        //TODO: needs to be implemented.
    }


    private void openMagnetometerSaving(@NotNull Measure measure) {
        //TODO: needs to be implemented.
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
