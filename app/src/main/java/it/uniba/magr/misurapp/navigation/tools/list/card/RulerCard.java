package it.uniba.magr.misurapp.navigation.tools.list.card;

import android.content.Context;

import androidx.navigation.NavController;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;

public class RulerCard implements MeasureCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_ruler;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_ruler;
    }

    @Override
    public int getDescriptionID() {
        return R.string.text_ruler_description;
    }

    @Override
    public void onClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;
        NavController navController = activity.getNavController();

        navController.navigate(R.id.action_nav_add_measure_fragment_to_ruler);

    }
    
}
