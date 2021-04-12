package it.uniba.magr.toolbox.tool.pedometer;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.navigation.ToolNavigationFragment;

public class PedometerFragment extends ToolNavigationFragment {


    public PedometerFragment() {
        super(new PedometerNavigation());
    }

    /*
     * Disable fullscreen mode.
     */
    @Override
    protected void fullscreen(@NotNull Activity activity) {
        // Nothing to do.
    }

}
