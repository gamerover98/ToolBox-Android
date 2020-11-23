package it.uniba.magr.misurapp.tool.ruler.save;

import it.uniba.magr.misurapp.navigation.save.SaveMeasureFragment;
import it.uniba.magr.misurapp.navigation.save.SaveMeasureNavigable;

public class SaveRulerNavigation implements SaveMeasureNavigable {

    @Override
    public int getParameterViewId() {
        return SaveMeasureFragment.NO_PARAMETERS;
    }

}
