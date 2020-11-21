package it.uniba.magr.misurapp.navigation.tools.card;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;

public class LevelCard implements MeasureCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_level;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_level;
    }

    @Override
    public int getDescriptionID() {
        return R.string.text_level_description;
    }

    @Override
    public void onClick(@NotNull Context context) {
        Toast.makeText(context, "level", Toast.LENGTH_SHORT).show();
    }

}
