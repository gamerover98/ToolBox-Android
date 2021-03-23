package it.uniba.magr.misurapp.navigation.main.recycle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.navigation.main.entry.MeasureEntry;

public class MeasureRecyclerGestureDetector extends GestureDetector.SimpleOnGestureListener {

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
                //TODO: needs to be implemented
                //measureEntry.onClick(recyclerView.getContext());
            }

        }

        return super.onContextClick(motionEvent);

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
