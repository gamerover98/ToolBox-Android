package it.uniba.magr.misurapp.util;

import android.content.Context;
import android.util.DisplayMetrics;

import org.jetbrains.annotations.NotNull;

public class GenericUtil {

    private GenericUtil() {
        throw new IllegalStateException("This is a static class");
    }

    /**
     * @param context A not null instance of a cotext.
     * @return Size in millimetres of one pixel of the device's display.
     */
    public static float getPixelSize(@NotNull Context context) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.xdpi * (1.0f / 25.4f);

    }

}
