package it.uniba.magr.misurapp.navigation.tools.list.card;

import android.content.Context;

import androidx.annotation.DrawableRes;

import org.jetbrains.annotations.NotNull;

public interface ToolCard {

    /**
     * @return The resource image id.
     */
    @DrawableRes
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
