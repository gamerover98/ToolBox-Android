package it.uniba.magr.misurapp.tool.barometer.save;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.realtime.NotConnectedException;
import it.uniba.magr.misurapp.database.realtime.RealtimeManager;
import it.uniba.magr.misurapp.database.realtime.bean.RealtimeBarometer;
import it.uniba.magr.misurapp.database.sqlite.SqliteManager;
import it.uniba.magr.misurapp.database.sqlite.bean.Barometer;
import it.uniba.magr.misurapp.database.sqlite.bean.Measure;
import it.uniba.magr.misurapp.database.sqlite.bean.Type;
import it.uniba.magr.misurapp.database.sqlite.dao.BarometersDao;
import it.uniba.magr.misurapp.navigation.save.SaveMeasureFragment;

import static it.uniba.magr.misurapp.tool.barometer.BarometerNavigation.BUNDLE_PRESSURE_KEY;

import java.util.Date;

public class SaveBarometerFragment extends SaveMeasureFragment {

    /**
     * The pressure of the barometer.
     */
    private float pressure;

    public SaveBarometerFragment() {
        super(() -> R.layout.fragment_save_barometer);
    }

    @NotNull
    @Override
    protected Type getMeasureType() {
        return Type.BAROMETER;
    }

    @Override
    protected void handleParametersCreation(@NotNull FragmentActivity activity, @NotNull Bundle bundle) {

        TextInputEditText pressureEditText = activity.findViewById(R.id.save_barometer_input_text_length);
        pressure = bundle.getFloat(BUNDLE_PRESSURE_KEY);

        String unit = activity.getString(R.string.barometer_unit);
        String lengthText = pressure + " " + unit;
        pressureEditText.setText(lengthText);

    }

    @Override
    protected void saveToSqlite(@NotNull SqliteManager sqliteManager, @NotNull Measure measure) {

        BarometersDao barometersDao = sqliteManager.barometersDao();
        Barometer barometer = new Barometer();

        barometer.setMeasureId(measure.getId());
        barometer.setPressure(pressure);

        barometersDao.insertBarometer(barometer);

    }

    @Override
    protected void saveToRealtime(@NotNull RealtimeManager realtimeManager,
                                  @NotNull Measure measure) throws NotConnectedException {

        int measureId      = measure.getId();
        String title       = measure.getTitle();
        String description = measure.getDescription();
        Date startDate     = measure.getStartDate();

        RealtimeBarometer realtimeBarometer = new RealtimeBarometer();

        realtimeBarometer.setMeasureId(measureId);
        realtimeBarometer.setTitle(title);
        realtimeBarometer.setDescription(description);
        realtimeBarometer.setStartDate(startDate);
        realtimeBarometer.setPressure(pressure);

        realtimeManager.addMeasure(realtimeBarometer);

    }

}
