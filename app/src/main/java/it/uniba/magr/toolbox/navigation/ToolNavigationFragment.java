package it.uniba.magr.toolbox.navigation;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.util.GenericUtil;

public class ToolNavigationFragment extends NavigationFragment {

    public ToolNavigationFragment(@NotNull Navigable navigable) {
        super(navigable);
    }

    @Override
    public void onAttach(@NotNull Context context) {

        super.onAttach(context);
        Activity activity = (Activity) context;
        fullscreen(activity);

    }

    @Override
    public void onDetach() {

        super.onDetach();

        Activity homeActivity = (Activity) getContext();
        assert homeActivity != null;

        Window window = homeActivity.getWindow();
        GenericUtil.showSystemUI(window);

    }

    protected void fullscreen(@NotNull Activity activity) {

        Window window = activity.getWindow();
        GenericUtil.hideSystemUI(window);

    }

}
