package it.uniba.magr.misurapp.navigation.edit.tool;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.edit.EditMeasureFragment;
import it.uniba.magr.misurapp.tool.barometer.BarometerNavigation;

public class EditBarometerFragment extends EditMeasureFragment {

    public EditBarometerFragment() {
        super(() -> R.layout.fragment_save_barometer);
    }

    @Override
    protected void handleParametersCreation(@NotNull FragmentActivity activity, @NotNull Bundle bundle) {

        TextInputEditText pressureEditText = activity.findViewById(R.id.save_barometer_input_text_length);
        float pressure = bundle.getFloat(BarometerNavigation.BUNDLE_PRESSURE_KEY);

        String unit = activity.getString(R.string.barometer_unit);
        String lengthText = pressure + " " + unit;
        pressureEditText.setText(lengthText);

    }

}
