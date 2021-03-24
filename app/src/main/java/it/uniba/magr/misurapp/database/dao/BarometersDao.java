package it.uniba.magr.misurapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import it.uniba.magr.misurapp.database.bean.Barometer;

/**
 * The Data access object (Dao) of barometer measurements.
 */
@Dao
public interface BarometersDao {

    /**
     * @param measureId The foreign key of the measure.
     * @return The barometer info.
     */
    @Query("SELECT * FROM Barometers WHERE measure_id=:measureId")
    Barometer getBarometer(int measureId);

    /**
     * @param barometer a not null barometer instance.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBarometer(Barometer barometer);

    /**
     * @param barometers a not null array of barometer instances.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBarometers(Barometer... barometers);

}