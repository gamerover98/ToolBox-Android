package it.uniba.magr.misurapp.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

/**
 * The Data access object (Dao) of measures.
 */
@Dao
public interface MeasurementsDao {

    /**
     * @return Gets all measurements that have been kept into the local database.
     */
    @Query("SELECT * FROM Measurements")
    List<Measure> getAll();

}
