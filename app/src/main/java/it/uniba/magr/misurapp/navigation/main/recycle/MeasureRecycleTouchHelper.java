package it.uniba.magr.misurapp.navigation.main.recycle;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.database.DatabaseManager;
import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.dao.MeasurementsDao;
import it.uniba.magr.misurapp.navigation.main.entry.MeasureEntry;

public class MeasureRecycleTouchHelper extends ItemTouchHelper.Callback {

    private static final int DRAG_DIRECTIONS  = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
    private static final int SWIPE_DIRECTIONS = ItemTouchHelper.RIGHT;

    private final MeasureRecyclerAdapter adapter;

    public MeasureRecycleTouchHelper(@NotNull MeasureRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NotNull RecyclerView recyclerView,
                                @NotNull RecyclerView.ViewHolder viewHolder) {
        return ItemTouchHelper.Callback.makeMovementFlags(DRAG_DIRECTIONS, SWIPE_DIRECTIONS);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView,
                          @NotNull RecyclerView.ViewHolder viewHolder,
                          @NotNull RecyclerView.ViewHolder target) {

        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition   = target.getAdapterPosition();

        adapter.onRowMoved(fromPosition, toPosition);

        Thread thread = new Thread(() -> {

            Context context = recyclerView.getContext();
            HomeActivity homeActivity = (HomeActivity) context;

            DatabaseManager databaseManager = homeActivity.getDatabaseManager();
            MeasurementsDao measurementsDao = databaseManager.measurementsDao();

            MeasureEntry e1 = adapter.getMeasureEntry(fromPosition);
            MeasureEntry e2 = adapter.getMeasureEntry(toPosition);

            if (e1 != null && e2 != null) {

                Measure m1 = e1.getMeasure();
                Measure m2 = e2.getMeasure();

                int temp = m1.getCardOrder();
                m1.setCardOrder(m2.getCardOrder());
                m2.setCardOrder(temp);

                measurementsDao.insertMeasurements(m1, m2);

            }

        });

        thread.start();
        return true;

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE
                && viewHolder instanceof MeasureRecyclerHolder) {

            MeasureRecyclerHolder holder = (MeasureRecyclerHolder) viewHolder;
            adapter.onRowSelected(holder);

        }

        super.onSelectedChanged(viewHolder, actionState);

    }

    @Override
    public void clearView(@NotNull RecyclerView recyclerView,
                          @NotNull RecyclerView.ViewHolder viewHolder) {

        super.clearView(recyclerView, viewHolder);

        if (!(viewHolder instanceof MeasureRecyclerHolder)) {
            return;
        }

        MeasureRecyclerHolder holder = (MeasureRecyclerHolder) viewHolder;
        adapter.onRowClear(holder);

    }

    @Override
    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.RIGHT) {

            adapter.removeMeasureEntry(position);
            adapter.updateRemoving(position);

        }

    }

}
