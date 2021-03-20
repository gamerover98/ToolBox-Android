package it.uniba.magr.misurapp.tool.ruler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.navigation.NavController;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class RulerNavigation implements Navigable {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ruler;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_ruler);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        if (activity instanceof HomeActivity) {

            FloatingActionButton saveButton = activity.findViewById(R.id.ruler_fab_button_save);
            saveButton.setOnClickListener(view -> onSaveButtonClick(activity));

        }

    }


    @Override
    public void onTouchEvent(@NotNull MotionEvent event) {
        Log.d("TEST", event.toString());
    }

    private void onSaveButtonClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;

        NavController navController = activity.getNavController();
        navController.navigate(R.id.action_nav_save_measure_fragment_to_ruler);

    }

}
