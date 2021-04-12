package it.uniba.magr.toolbox.tool.magnetometer;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.navigation.ToolNavigationFragment;

public class MagnetometerFragment extends ToolNavigationFragment {


    public MagnetometerFragment() {
        super(new MagnetometerNavigation());
    }

    /*
     * Disable fullscreen mode.
     */
    @Override
    protected void fullscreen(@NotNull Activity activity) {
        // Nothing to do.
    }

}
