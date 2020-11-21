package it.uniba.magr.misurapp.navigation.tools.card;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

public interface MeasureCard {

    /**
     * @return The resource image id.
     */
    int getImageID();

    /**
     * @return The resource string title id.
     */
    int getTitleID();

    /**
     * @return the resource string description id.
     */
    int getDescriptionID();

    /**
     * Perform the click event.
     */
    void onClick(@NotNull Context context);

}
