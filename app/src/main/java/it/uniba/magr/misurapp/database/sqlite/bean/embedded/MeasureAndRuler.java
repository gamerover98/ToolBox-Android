package it.uniba.magr.misurapp.database.sqlite.bean.embedded;

import androidx.room.Embedded;
import androidx.room.Relation;

import it.uniba.magr.misurapp.database.sqlite.bean.Measure;
import it.uniba.magr.misurapp.database.sqlite.bean.Ruler;
import lombok.Getter;

/**
 * An embedded class of: Measurements JOIN Rulers.
 */
public class MeasureAndRuler {

    @Getter @Embedded
    private Measure measure;

    @Getter
    @Relation(
            parentColumn = "id",
            entityColumn = "measure_id"
    )
    private Ruler ruler;

    /*
     * Doesn't work with @Setter lombok.
     */
    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    /*
     * Doesn't work with @Setter lombok.
     */
    public void setRuler(Ruler ruler) {
        this.ruler = ruler;
    }

}
