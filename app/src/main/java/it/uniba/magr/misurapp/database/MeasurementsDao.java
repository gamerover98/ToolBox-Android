package it.uniba.magr.misurapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * The Data access object (Dao) of measures.
 */
@Dao
public interface MeasurementsDao {

    /**
     * @param title The title of the
     * @return A list of measurements filtered by title.
     */
    @Query("SELECT * FROM Measurements WHERE title=:title")
    List<Measure> getMeasures(String title);

    /**
     * @return Gets all measurements that have been kept into the local database.
     */
    @Query("SELECT * FROM Measurements")
    List<Measure> getAll();

    /**
     * @param measure a not null measure instance.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeasure(Measure measure);

    /**
     * @param measures a not null array of measurements instances.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeasurements(Measure... measures);

}
