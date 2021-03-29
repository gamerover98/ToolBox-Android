package it.uniba.magr.misurapp.tool.ruler.save;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.DatabaseManager;
import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.bean.Ruler;
import it.uniba.magr.misurapp.database.bean.Type;
import it.uniba.magr.misurapp.database.dao.RulersDao;
import it.uniba.magr.misurapp.navigation.save.SaveMeasureFragment;
import it.uniba.magr.misurapp.tool.ruler.RulerNavigation;
import it.uniba.magr.misurapp.util.GenericUtil;

@SuppressWarnings("unused") // unused methods
public class SaveRulerFragment extends SaveMeasureFragment {

    /**
     * The length of the ruler measure.
     */
    private float length;

    public SaveRulerFragment() {
        super(() -> R.layout.fragment_save_ruler);
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

    @NotNull
    @Override
    protected Type getMeasureType() {
        return Type.RULER;
    }

    @Override
    protected void handleParametersCreation(@NotNull FragmentActivity activity, @NotNull Bundle bundle) {

        TextInputEditText lengthEditText = activity.findViewById(R.id.save_ruler_input_text_length);
        length = bundle.getFloat(RulerNavigation.BUNDLE_LENGTH_KEY);

        String lengthText = length + " cm";
        lengthEditText.setText(lengthText);

    }

    @Override
    protected void save(@NotNull DatabaseManager databaseManager, @NotNull Measure measure) {

        RulersDao rulersDao = databaseManager.rulersDao();
        Ruler ruler = new Ruler();

        ruler.setMeasureId(measure.getId());
        ruler.setLength(length);

        rulersDao.insertRuler(ruler);

    }

}
