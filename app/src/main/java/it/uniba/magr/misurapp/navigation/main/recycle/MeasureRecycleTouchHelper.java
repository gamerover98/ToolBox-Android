package it.uniba.magr.misurapp.navigation.main.recycle;

import android.util.Log;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class MeasureRecycleTouchHelper extends ItemTouchHelper.Callback {

    private static final int DRAG_DIRECTIONS  = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
    private static final int SWIPE_DIRECTIONS = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;

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
        Log.d("TEST", "position: " + position + " direction: " + direction);
        adapter.update(position);

    }

}
