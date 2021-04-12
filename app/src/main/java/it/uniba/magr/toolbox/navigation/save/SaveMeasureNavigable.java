package it.uniba.magr.toolbox.navigation.save;

import android.content.Context;

import androidx.annotation.LayoutRes;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.R;
import it.uniba.magr.toolbox.navigation.Navigable;

public interface SaveMeasureNavigable extends Navigable {

    @Override
    default int getLayoutId() {
        return R.layout.fragment_save_measure;
    }

    @NotNull
    @Override
    default String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_save_measure);
    }

    /**
     * @return The GroupView ID of items that will
     * be added to the generic save measure fragment layout.
     */
    @LayoutRes
    int getParameterViewId();

}