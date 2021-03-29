package it.uniba.magr.misurapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.bean.embedded.MeasureAndBarometer;
import it.uniba.magr.misurapp.database.bean.embedded.MeasureAndMagnetometer;
import it.uniba.magr.misurapp.database.bean.embedded.MeasureAndRuler;

/**
 * The Data access object (Dao) of measures.
 */
@Dao
public interface MeasurementsDao {

    /**
     * @return Gets all measurements that have been kept into the local database.
     */
    @Query("SELECT * FROM Measurements ORDER BY card_order")
    List<Measure> getAll();

    /**
     * Gets a measure from its id.
     *
     * @param id the measure id.
     * @return The measure instance.
     */
    @Query("SELECT * FROM Measurements WHERE id=:id")
    Measure getMeasure(int id);

    /**
     * @param title The title of the
     * @return A list of measurements filtered by title.
     */
    @Query("SELECT * FROM Measurements WHERE title=:title ORDER BY card_order")
    List<Measure> getMeasurements(String title);

    /**
     * @param id The id of an existing measure.
     * @return A list of measurements filtered by title.
     */
    @Transaction
    @Query("SELECT * FROM Measurements NATURAL JOIN Rulers WHERE id=:id ORDER BY card_order")
    List<MeasureAndRuler> getRulerMeasure(int id);

    /**
     * @param title The title of the
     * @return A list of measurements filtered by title.
     */
    @Transaction
    @Query("SELECT * FROM Measurements NATURAL JOIN Barometers WHERE title=:title ORDER BY card_order")
    List<MeasureAndBarometer> getBarometerMeasure(String title);

    /**
     * @param title The title of the
     * @return A list of measurements filtered by title.
     */
    @Transaction
    @Query("SELECT * FROM Measurements NATURAL JOIN Magnetometers WHERE title=:title ORDER BY card_order")
    List<MeasureAndMagnetometer> getMagnetometersMeasure(String title);

    /**
     * @param measure a not null measure instance.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeasure(Measure measure);

    /**
     * @param measurements a not null array of measurements instances.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeasurements(Measure... measurements);

    /**
     * @param measure a not null measure instance.
     */
    @Update
    void updateMeasure(Measure measure);

    /**
     * @param measure a not null measure instance.
     */
    @Delete
    void removeMeasure(Measure measure);

    /**
     * @param measurements A not null array of measurements instances.
     */
    @Delete
    void removeMeasurements(Measure... measurements);

    /**
     * @return The latest measure ID added into the table.
     */
    @Query("SELECT MAX(id) FROM Measurements")
    int getLatestMeasureID();

    /**
     * @return The max order value into the table.
     */
    @Query("SELECT MAX(card_order) FROM Measurements")
    int getMaxOrder();

}
