package it.uniba.magr.toolbox.navigation.tools.list.card;

import android.content.Context;

import androidx.navigation.NavController;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.HomeActivity;
import it.uniba.magr.toolbox.R;

public class RulerToolCard implements ToolCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_ruler;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_ruler;
    }

    @Override
    public void onClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;
        NavController navController = activity.getNavController();

        navController.navigate(R.id.action_nav_add_measure_fragment_to_ruler);

    }

    @Override
    public boolean isSupported(@NotNull Context context) {
        return true;
    }

}
