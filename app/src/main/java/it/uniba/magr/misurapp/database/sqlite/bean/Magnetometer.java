package it.uniba.magr.misurapp.database.sqlite.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import lombok.ToString;

@ToString
@Entity(
        tableName = "Magnetometers",
        primaryKeys = {"measure_id", "count"},
        foreignKeys = {
                @ForeignKey(
                        entity        = Measure.class,
                        parentColumns = {"id"},            // Measurements.id
                        childColumns  = {"measure_id"},    // Magnetometers.measure_id
                        onDelete      = ForeignKey.CASCADE,
                        onUpdate      = ForeignKey.CASCADE
                )
        }
)
public class Magnetometer {

    /**
     * The measure_id foreign and primary key of this table.
     */
    @ColumnInfo(name = "measure_id")
    private int measureId;

    /**
     * The count.
     */
    @ColumnInfo(name = "count")
    private int count;

    /**
     * The tesla value.
     */
    @ColumnInfo(name = "value")
    private double value;

    /**
     * The second of this value.
     */
    @ColumnInfo(name = "time")
    private int time;

    //
    // GETTERS
    //

    public int getMeasureId() {
        return this.measureId;
    }

    public int getCount() {
        return this.count;
    }

    public double getValue() {
        return this.value;
    }

    public int getTime() {
        return this.time;
    }

    //
    // SETTERS
    //

    public void setMeasureId(int measureId) {
        this.measureId = measureId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
