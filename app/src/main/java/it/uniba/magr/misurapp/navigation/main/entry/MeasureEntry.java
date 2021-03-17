package it.uniba.magr.misurapp.navigation.main.entry;

import android.content.Context;

import androidx.annotation.DrawableRes;

import org.jetbrains.annotations.NotNull;

/**
 * Create an entry for the main list view of measures.
 */
public interface MeasureEntry {

    /**
     * @return The resource image ID of the tool icon.
     */
    @DrawableRes
    int getImageID();

    /**
     * @return The measure title.
     */
    String getTitle();

    /**
     * @return the measure description.
     */
    String getDescription();

    /**
     * Perform the click event.
     * @param context The not null instance of the context.
     */
    void onClick(@NotNull Context context);

}
