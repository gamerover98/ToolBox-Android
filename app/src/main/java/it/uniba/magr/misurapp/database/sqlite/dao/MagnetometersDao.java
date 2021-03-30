package it.uniba.magr.misurapp.database.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.uniba.magr.misurapp.database.sqlite.bean.Magnetometer;

/**
 * The Data access object (Dao) of magnetometer measurements.
 */
@Dao
public interface MagnetometersDao {

    /**
     * @param measureId The foreign key of the measure.
     * @return The magnetometer info.
     */
    @Query("SELECT * FROM Magnetometers WHERE measure_id=:measureId")
    List<Magnetometer> getMagnetometers(int measureId);

    /**
     * @param magnetometer a not null magnetometer instance.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMagnetometer(Magnetometer magnetometer);

    /**
     * @param magnetometers a not null array of magnetometer instances.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMagnetometers(Magnetometer... magnetometers);

}