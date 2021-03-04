package it.uniba.magr.misurapp.navigation.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

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

    private FloatingActionButton buttonAddMeasure;

    private MaterialTextView buttonAddMeasureTextView;

    private Animation animationFabClosing;
    private Animation animationFabOpening;
    private Animation animationRotateBackward;
    private Animation animationRotateForward;

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

        // click toolbar title
        homeActivity.getToolbar().setOnClickListener(this :: performMainLayoutClick);

        // click navbar button
        homeActivity.setNavigationButtonClick(this :: navigationButtonClick);

    }

    private void setupFloatingButtons() {

        FloatingActionButton fabButton = homeActivity.findViewById(R.id.fab_button_operation);

        buttonAddMeasure = homeActivity.findViewById(R.id.fab_button_add_measure);
        animationFabOpening = AnimationUtils.loadAnimation(homeActivity, R.anim.fab_open);
        animationFabClosing = AnimationUtils.loadAnimation(homeActivity, R.anim.fab_close);
        animationRotateForward  = AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_forward);
        animationRotateBackward = AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_backward);

        buttonAddMeasureTextView = homeActivity.findViewById(R.id.fab_button_add_measure_text_view);
        buttonAddMeasure.setOnClickListener(this :: performAddMeasureClick);

        fabButton.setOnClickListener(this :: performFabClick);

    }

    public void performMainLayoutClick(@NotNull View view) {

        FloatingActionButton fabButton = homeActivity.findViewById(R.id.fab_button_operation);

        if (fabOpened) {
            closeFabButton(fabButton);
        }

    }

    private void navigationButtonClick() {

        View mainLayout = homeActivity.findViewById(R.id.fragment_main_layout);

        if (mainLayout != null) {
            performMainLayoutClick(mainLayout);
        }

    }

    private void performAddMeasureClick(@NotNull View view) {

        homeActivity.getNavController().navigate(R.id.nav_list_tools_fragment);
        homeActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private void performFabClick(@NotNull View view) {

        FloatingActionButton fabButton = homeActivity.findViewById(R.id.fab_button_operation);

        if (fabOpened) {
            closeFabButton(fabButton);
        } else {
            openFabButton(fabButton);
        }

    }

    /**
     * Close the floating button with its properly animation.
     * @param fabButton The not null instance of the floating button.
     */
    private void closeFabButton(@NotNull FloatingActionButton fabButton) {

        fabButton.startAnimation(animationRotateBackward);

        buttonAddMeasure.startAnimation(animationFabClosing);
        buttonAddMeasureTextView.setVisibility(View.INVISIBLE);
        buttonAddMeasure.setClickable(false);

        fabOpened = false;

    }

    /**
     * Open the floating button with its properly animation.
     * @param fabButton The not null instance of the floating button.
     */
    private void openFabButton(@NotNull FloatingActionButton fabButton) {

        fabButton.startAnimation(animationRotateForward);

        buttonAddMeasure.startAnimation(animationFabOpening);
        buttonAddMeasureTextView.setVisibility(View.VISIBLE);
        buttonAddMeasure.setClickable(true);

        fabOpened = true;

    }

}
