package it.uniba.magr.misurapp.navigation;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;

import org.jetbrains.annotations.NotNull;

public class ToolNavigationFragment extends NavigationFragment {

    public ToolNavigationFragment(@NotNull Navigable navigable) {
        super(navigable);
    }

    @Override
    public void onAttach(@NotNull Context context) {

        super.onAttach(context);
        Activity activity = (Activity) context;

        Window window = activity.getWindow();
        View decoder = window.getDecorView();

        int uiProperties = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decoder.setSystemUiVisibility(uiProperties);

    }

    @Override
    public void onDetach() {

        super.onDetach();

        Activity homeActivity = (Activity) getContext();
        assert homeActivity != null;

        Window window = homeActivity.getWindow();
        View decoder = window.getDecorView();

        int uiProperties = View.SYSTEM_UI_FLAG_VISIBLE;
        decoder.setSystemUiVisibility(uiProperties);

    }

}
