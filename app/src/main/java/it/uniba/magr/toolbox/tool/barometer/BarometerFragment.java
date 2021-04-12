package it.uniba.magr.toolbox.tool.barometer;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.navigation.ToolNavigationFragment;

public class BarometerFragment extends ToolNavigationFragment {

    public BarometerFragment() {
        super(new BarometerNavigation());
    }

    /*
     * Disable fullscreen mode.
     */
    @Override
    protected void fullscreen(@NotNull Activity activity) {
        // Nothing to do.
    }

}
