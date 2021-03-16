package it.uniba.magr.misurapp.tool.magnetometer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;

public class MagnetometerNavigation implements Navigable {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_magnetometer;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_magnetometer);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {
        // Nothing to do.
    }

}
