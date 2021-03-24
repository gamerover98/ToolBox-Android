package it.uniba.magr.misurapp.database.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.ToString;

@ToString
@Entity(
        tableName = "Barometers",
        foreignKeys = {
                @ForeignKey(
                        entity        = Measure.class,
                        parentColumns = {"id"},            // Measurements.id
                        childColumns  = {"measure_id"},    // Barometers.measure_id
                        onDelete      = ForeignKey.CASCADE,
                        onUpdate      = ForeignKey.CASCADE
                )
        }
)
public class Barometer {

    /**
     * The measure_id foreign and primary key of this table.
     */
    @PrimaryKey()
    @ColumnInfo(name = "measure_id")
    private int measureId;

    /**
     * The pressure in hPa.
     */
    @ColumnInfo(name = "pressure")
    private double pressure;

    public int getMeasureId() {
        return this.measureId;
    }

    public double getPressure() {
        return this.pressure;
    }

    public void setMeasureId(int measureId) {
        this.measureId = measureId;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

}
