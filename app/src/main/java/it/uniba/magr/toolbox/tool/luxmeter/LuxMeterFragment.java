package it.uniba.magr.toolbox.tool.luxmeter;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.navigation.ToolNavigationFragment;

public class LuxMeterFragment extends ToolNavigationFragment {


    public LuxMeterFragment() {
        super(new LuxMeterNavigation());
    }

    /*
     * Disable fullscreen mode.
     */
    @Override
    protected void fullscreen(@NotNull Activity activity) {
        // Nothing to do.
    }

}
