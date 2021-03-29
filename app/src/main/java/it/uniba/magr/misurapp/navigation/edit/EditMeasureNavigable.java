package it.uniba.magr.misurapp.navigation.edit;

import android.content.Context;

import androidx.annotation.LayoutRes;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public interface EditMeasureNavigable extends Navigable {

    @Override
    default int getLayoutId() {
        return R.layout.fragment_edit_measure;
    }

    @NotNull
    @Override
    default String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_edit_measure);
    }

    /**
     * @return The GroupView ID of items that will
     * be added to the generic edit measure fragment layout.
     */
    @LayoutRes
    int getParameterViewId();

}
