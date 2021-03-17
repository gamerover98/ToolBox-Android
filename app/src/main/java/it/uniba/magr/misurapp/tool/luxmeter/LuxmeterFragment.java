package it.uniba.magr.misurapp.tool.luxmeter;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.navigation.ToolNavigationFragment;

public class LuxmeterFragment extends ToolNavigationFragment {


    public LuxmeterFragment() {
        super(new LuxmeterNavigation());
    }

    /*
     * Disable fullscreen mode.
     */
    @Override
    protected void fullscreen(@NotNull Activity activity) {
        // Nothing to do.
    }

}
