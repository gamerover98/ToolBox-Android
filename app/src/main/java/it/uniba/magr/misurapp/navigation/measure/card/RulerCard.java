package it.uniba.magr.misurapp.navigation.measure.card;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

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
        Toast.makeText(context, "ruler", Toast.LENGTH_SHORT).show();
    }
    
}
