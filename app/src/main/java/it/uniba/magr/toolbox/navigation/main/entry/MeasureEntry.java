package it.uniba.magr.toolbox.navigation.main.entry;

import androidx.annotation.DrawableRes;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.database.sqlite.bean.Measure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Create an entry for the main list view of measures.
 */
@RequiredArgsConstructor
public class MeasureEntry {

    /**
     * The resource image ID of the tool icon.
     */
    @Getter @DrawableRes
    private final int imageID;

    /**
     * The measure instance.
     */
    @Getter @NotNull
    private final Measure measure;

}
