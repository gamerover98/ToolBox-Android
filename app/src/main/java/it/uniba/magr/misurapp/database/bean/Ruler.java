package it.uniba.magr.misurapp.database.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.ToString;

@ToString
@Entity(
        tableName = "Rulers",
        foreignKeys = {
                @ForeignKey(
                        entity        = Measure.class,
                        parentColumns = {"id"},            // Measurements.id
                        childColumns  = {"measure_id"},    // Rulers.measure_id
                        onDelete      = ForeignKey.CASCADE,
                        onUpdate      = ForeignKey.CASCADE
                )
        }
)
public class Ruler {

    /**
     * The measure_id foreign and primary key of this table.
     */
    @PrimaryKey()
    @ColumnInfo(name = "measure_id")
    private int measureId;

    /**
     * The length in centimeters.
     */
    @ColumnInfo(name = "length", defaultValue = "0")
    private double length = 0;

    public int getMeasureId() {
        return this.measureId;
    }

    public double getLength() {
        return this.length;
    }

    public void setMeasureId(int measureId) {
        this.measureId = measureId;
    }

    public void setLength(double length) {
        this.length = length;
    }

}
