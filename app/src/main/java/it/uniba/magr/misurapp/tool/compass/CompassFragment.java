package it.uniba.magr.misurapp.tool.compass;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.navigation.ToolNavigationFragment;

public class CompassFragment extends ToolNavigationFragment {


    public CompassFragment() {
        super(new CompassNavigation());
    }

    /*
     * Disable fullscreen mode.
     */
    @Override
    protected void fullscreen(@NotNull Activity activity) {
        // Nothing to do.
    }

}
