package it.uniba.magr.misurapp.navigation.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;
import lombok.Getter;

public class MainNavigation implements Navigable {

    /**
     * Gets a static access to this instance.
     */
    @Getter
    private static MainNavigation instance;

    private HomeActivity homeActivity;

    boolean fabOpened = false;

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
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        fabOpened = false;
        homeActivity = (HomeActivity) activity;

        setupFloatingButtons();

        GridView gridView = activity.findViewById(R.id.measure_list_grid_view);
        assert gridView != null;

        gridView.setAdapter(new ListMeasuresCardAdapter(homeActivity));

        DrawerLayout drawerLayout = homeActivity.getDrawerLayout();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    private void setupFloatingButtons() {

        FloatingActionButton fabButton = homeActivity.findViewById(R.id.fab_button_operation);
        fabButton.setOnClickListener(this :: performAddMeasureClick);

    }

    private void performAddMeasureClick(@NotNull View view) {

        homeActivity.getNavController().navigate(R.id.nav_list_tools_fragment);
        homeActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    public void performMeasureGridItemClick(@NotNull View view) {
        // TODO: will be implemented soon.
    }

}
