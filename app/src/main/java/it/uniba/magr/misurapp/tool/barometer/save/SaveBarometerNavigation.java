package it.uniba.magr.misurapp.tool.barometer.save;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.save.SaveMeasureNavigable;

public class SaveBarometerNavigation implements SaveMeasureNavigable {

    @Override
    public int getParameterViewId() {
        return R.layout.fragment_save_barometer;
    }

}
