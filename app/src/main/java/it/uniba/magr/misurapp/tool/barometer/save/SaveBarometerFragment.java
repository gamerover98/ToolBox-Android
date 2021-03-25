package it.uniba.magr.misurapp.tool.barometer.save;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.DatabaseManager;
import it.uniba.magr.misurapp.database.bean.Barometer;
import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.bean.Type;
import it.uniba.magr.misurapp.database.dao.BarometersDao;
import it.uniba.magr.misurapp.navigation.save.SaveMeasureFragment;
import it.uniba.magr.misurapp.tool.barometer.BarometerNavigation;

public class SaveBarometerFragment extends SaveMeasureFragment {

    /**
     * The pressure of the barometer.
     */
    private float pressure;

    public SaveBarometerFragment() {
        super(new SaveBarometerNavigation());
    }

    @NotNull
    @Override
    protected Type getMeasureType() {
        return Type.BAROMETER;
    }

    @Override
    protected void handleParametersCreation(@NotNull FragmentActivity activity, @NotNull Bundle bundle) {

        TextInputEditText pressureEditText = activity.findViewById(R.id.save_barometer_input_text_length);
        pressure = bundle.getFloat(BarometerNavigation.BUNDLE_PRESSURE_KEY);

        String unit = activity.getString(R.string.barometer_unit);
        String lengthText = pressure + " " + unit;
        pressureEditText.setText(lengthText);

    }

    @Override
    protected void save(@NotNull DatabaseManager databaseManager, @NotNull Measure measure) {

        BarometersDao barometersDao = databaseManager.barometersDao();
        Barometer barometer = new Barometer();

        barometer.setMeasureId(measure.getId());
        barometer.setPressure(pressure);

        barometersDao.insertBarometer(barometer);

    }

}
