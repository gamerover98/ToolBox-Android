package it.uniba.magr.misurapp.navigation.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
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
    private static MainNavigation instance;

    /**
     * Gets the instance of the home activity.
     */
    @Getter
    private HomeActivity homeActivity;

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
    @SuppressWarnings("ClickableViewAccessibility")
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        setupFloatingButtons();

        RecyclerView recyclerView = activity.findViewById(R.id.main_measure_recycler_view);
        assert recyclerView != null;

        MeasureRecyclerAdapter adapter = new MeasureRecyclerAdapter(recyclerView);
        GestureDetectorCompat recyclerGestureDetector = new GestureDetectorCompat(
                homeActivity, new MeasureRecyclerGestureDetector(recyclerView));
        MeasureRecyclerGestureListener gestureListener =
                new MeasureRecyclerGestureListener(recyclerGestureDetector);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity);

        recyclerView.setAdapter            (adapter);
        recyclerView.setLayoutManager      (linearLayoutManager);
        recyclerView.addOnItemTouchListener(gestureListener);
        recyclerView.setHasFixedSize       (true);

        // drag, right and left swipes.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MeasureRecycleTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        DrawerLayout drawerLayout = homeActivity.getDrawerLayout();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    @Override
    @SuppressWarnings("squid:S2696")
    public void onDestroyView() {
        instance = null;
    }



    private void setupFloatingButtons() {

        FloatingActionButton fabButton = homeActivity.findViewById(R.id.fab_button_operation);
        fabButton.setOnClickListener(this :: performAddMeasureClick);

    }

    private void performAddMeasureClick(@NotNull View view) {

        homeActivity.getNavController().navigate(R.id.nav_list_tools_fragment);
        homeActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

}
