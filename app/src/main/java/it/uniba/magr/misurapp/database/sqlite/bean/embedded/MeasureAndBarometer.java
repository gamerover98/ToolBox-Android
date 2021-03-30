package it.uniba.magr.misurapp.database.sqlite.bean.embedded;

import androidx.room.Embedded;
import androidx.room.Relation;

import it.uniba.magr.misurapp.database.sqlite.bean.Barometer;
import it.uniba.magr.misurapp.database.sqlite.bean.Measure;
import lombok.Getter;

/**
 * An embedded class of: Measurements JOIN Barometers.
 */
public class MeasureAndBarometer {

    @Getter @Embedded
    private Measure measure;

    @Getter
    @Relation(
            parentColumn = "id",
            entityColumn = "measure_id"
    )
    private Barometer barometer;

    /*
     * Doesn't work with @Setter lombok.
     */
    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    /*
     * Doesn't work with @Setter lombok.
     */
    public void setBarometer(Barometer barometer) {
        this.barometer = barometer;
    }

}
