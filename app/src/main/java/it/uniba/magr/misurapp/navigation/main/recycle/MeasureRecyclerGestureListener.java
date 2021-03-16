package it.uniba.magr.misurapp.navigation.main.recycle;

import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeasureRecyclerGestureListener extends RecyclerView.SimpleOnItemTouchListener {

    private final GestureDetectorCompat gestureDetector;

    @Override
    public boolean onInterceptTouchEvent(@NotNull RecyclerView recyclerView,
                                         @NotNull MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public void onTouchEvent(@NotNull RecyclerView recyclerView,
                             @NotNull MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
    }

}
