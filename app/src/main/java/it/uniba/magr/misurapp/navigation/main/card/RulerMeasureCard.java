package it.uniba.magr.misurapp.navigation.main.card;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;

public class RulerMeasureCard implements MeasureCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_ruler;
    }

    @Override
    public String getTitle() {
        return "test title";
    }

    @Override
    public String getDescription() {
        return "test description";
    }

    @Override
    public void onClick(@NotNull Context context) {
        // nothing to do
    }

}
