package it.uniba.magr.misurapp.navigation.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class MainNavigation implements Navigable {

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
        return R.layout.fragment_main_layout;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.app_name);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        fabOpened = false;
        homeActivity = (HomeActivity) activity;

        setupFloatingButtons();

        DrawerLayout drawerLayout = homeActivity.getDrawerLayout();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    private void setupFloatingButtons() {

        FloatingActionButton buttonOperation = homeActivity.findViewById(R.id.fab_button_operation);

        buttonAddMeasure = homeActivity.findViewById(R.id.fab_button_add_measure);
        animationFabOpening = AnimationUtils.loadAnimation(homeActivity, R.anim.fab_open);
        animationFabClosing = AnimationUtils.loadAnimation(homeActivity, R.anim.fab_close);
        animationRotateForward  = AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_forward);
        animationRotateBackward = AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_backward);

        buttonAddMeasureTextView = homeActivity.findViewById(R.id.fab_button_add_measure_text_view);
        buttonAddMeasure.setOnClickListener(this :: addMeasureClick);

        buttonOperation.setOnClickListener(this :: operationButtonClick);

    }

    private void addMeasureClick(@NotNull View view) {

        homeActivity.getNavController().navigate(R.id.nav_list_tools_fragment);
        homeActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private void operationButtonClick(@NotNull View view) {

        if (fabOpened) {

            view.startAnimation(animationRotateBackward);

            buttonAddMeasure.startAnimation(animationFabClosing);
            buttonAddMeasureTextView.setVisibility(View.INVISIBLE);
            buttonAddMeasure.setClickable(false);

            fabOpened = false;

        } else {

            view.startAnimation(animationRotateForward);

            buttonAddMeasure.startAnimation(animationFabOpening);
            buttonAddMeasureTextView.setVisibility(View.VISIBLE);
            buttonAddMeasure.setClickable(true);

            fabOpened = true;

        }

    }

}
