package it.uniba.magr.misurapp.tool.ruler.save;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.save.SaveMeasureFragment;
import it.uniba.magr.misurapp.util.GenericUtil;

import static it.uniba.magr.misurapp.util.GenericUtil.*;

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

    /**
     * @return the inserted centimeters length.
     */
    public int getLength() {

        assert getActivity() != null;
        return getIntFromInputLayout(getActivity(), R.id.save_ruler_input_text_box_length);

    }

    /**
     * Set the length in centimeters.
     */
    public void setLength(int value) {

        assert getActivity() != null;
        setIntToInputLayout(getActivity(), R.id.save_ruler_input_text_box_length, value);

    }

}
