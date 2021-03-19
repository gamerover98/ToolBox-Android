package it.uniba.magr.misurapp.tool.pedometer;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.navigation.ToolNavigationFragment;

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
