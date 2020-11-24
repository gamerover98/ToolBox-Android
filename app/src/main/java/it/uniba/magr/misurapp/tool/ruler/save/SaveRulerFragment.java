package it.uniba.magr.misurapp.tool.ruler.save;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.navigation.save.SaveMeasureFragment;
import it.uniba.magr.misurapp.util.GenericUtil;

public class SaveRulerFragment extends SaveMeasureFragment {

    public SaveRulerFragment() {
        super(new SaveRulerNavigation());
    }

    @Override
    public void onAttach(@NotNull Context context) {

        super.onAttach(context);
        Activity activity = (Activity) context;

        Window window = activity.getWindow();
        GenericUtil.showSystemUI(window);

    }

    @Override
    public void onDetach() {

        super.onDetach();

        Activity homeActivity = (Activity) getContext();
        assert homeActivity != null;

        Window window = homeActivity.getWindow();
        GenericUtil.hideSystemUI(window);

    }

}
