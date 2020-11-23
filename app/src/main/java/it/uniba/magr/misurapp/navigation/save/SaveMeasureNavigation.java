package it.uniba.magr.misurapp.navigation.save;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class SaveMeasureNavigation implements Navigable {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_save_measure;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_save_measure);
    }

}
