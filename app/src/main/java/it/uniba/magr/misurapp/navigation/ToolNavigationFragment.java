package it.uniba.magr.misurapp.navigation;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.util.GenericUtil;

public class ToolNavigationFragment extends NavigationFragment {

    public ToolNavigationFragment(@NotNull Navigable navigable) {
        super(navigable);
    }

    @Override
    public void onAttach(@NotNull Context context) {

        super.onAttach(context);
        Activity activity = (Activity) context;

        Window window = activity.getWindow();
        GenericUtil.hideSystemUI(window);

    }

    @Override
    public void onDetach() {

        super.onDetach();

        Activity homeActivity = (Activity) getContext();
        assert homeActivity != null;

        Window window = homeActivity.getWindow();
        GenericUtil.showSystemUI(window);

    }

}
