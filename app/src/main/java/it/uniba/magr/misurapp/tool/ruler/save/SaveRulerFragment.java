package it.uniba.magr.misurapp.tool.ruler.save;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.DatabaseManager;
import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.bean.Ruler;
import it.uniba.magr.misurapp.database.dao.RulersDao;
import it.uniba.magr.misurapp.navigation.save.SaveMeasureFragment;
import it.uniba.magr.misurapp.util.GenericUtil;

import static it.uniba.magr.misurapp.util.GenericUtil.*;

@SuppressWarnings("unused") // unused methods
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

    @Override
    protected void handleParametersCreation() {
        // nothing to do
    }

    @Override
    protected void save(@NotNull DatabaseManager databaseManager, @NotNull Measure measure) {

        RulersDao rulersDao = databaseManager.rulersDao();
        Ruler ruler = new Ruler();

        ruler.setMeasureId(measure.getId());
        ruler.setLength(getLength());

        rulersDao.insertRuler(ruler);

    }

    /**
     * @return the inserted centimeters length.
     */
    public double getLength() {

        assert getActivity() != null;
        return getNumberFromInputLayout(getActivity(),
                R.id.save_ruler_input_text_box_length).doubleValue();

    }

    /**
     * Set the length in centimeters.
     */
    public void setLength(double value) {

        assert getActivity() != null;

        if (value == (long) value) {

            setNumberToInputLayout(getActivity(),
                    R.id.save_ruler_input_text_box_length, (long) value);

        } else {

            setNumberToInputLayout(getActivity(),
                    R.id.save_ruler_input_text_box_length, value);

        }

    }

}
