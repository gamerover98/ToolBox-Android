package it.uniba.magr.misurapp.tool.magnetometer;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.navigation.ToolNavigationFragment;

public class MagnetometerFragment extends ToolNavigationFragment {

    public MagnetometerFragment() {
        super(new MagnetometerNavigation());
    }

    @Override
    protected void fullscreen(@NotNull Activity activity) {
        // Nothing to do.
    }

}
