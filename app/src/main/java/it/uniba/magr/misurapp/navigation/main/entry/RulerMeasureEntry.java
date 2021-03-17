package it.uniba.magr.misurapp.navigation.main.entry;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;

public class RulerMeasureEntry implements MeasureEntry {

    public static int COUNT = 0; //TODO: remove it after tests

    @Override
    public int getImageID() {
        return R.drawable.icon_ruler;
    }

    @Override
    public String getTitle() {
        return "test title " + COUNT++;
    }

    @Override
    public String getDescription() {
        return "test description";
    }

    @Override
    public void onClick(@NotNull Context context) {
        Toast.makeText(context, getTitle(), Toast.LENGTH_SHORT).show();
    }

}
