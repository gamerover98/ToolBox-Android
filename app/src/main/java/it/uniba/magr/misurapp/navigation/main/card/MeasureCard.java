package it.uniba.magr.misurapp.navigation.main.card;

import android.content.Context;

import androidx.annotation.DrawableRes;

import org.jetbrains.annotations.NotNull;

public interface MeasureCard {

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

    /**
     * Perform the edit button.
     * @param context The not null instance of the context.
     */
    default void onEdit(@NotNull Context context) {
        // nothing to do.
    }

}
