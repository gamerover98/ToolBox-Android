package it.uniba.magr.misurapp.navigation.tools.list.card;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;

public class LevelToolCard implements ToolCard {

    @Override
    public int getImageID() {
        return R.drawable.icon_level;
    }

    @Override
    public int getTitleID() {
        return  R.string.text_level;
    }

    @Override
    public void onClick(@NotNull Context context) {
        Toast.makeText(context, "level", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isSupported(@NotNull Context context) {
        return false;
    }

}
