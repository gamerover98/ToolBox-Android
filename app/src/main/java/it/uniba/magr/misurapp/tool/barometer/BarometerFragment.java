package it.uniba.magr.misurapp.tool.barometer;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.navigation.ToolNavigationFragment;

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
