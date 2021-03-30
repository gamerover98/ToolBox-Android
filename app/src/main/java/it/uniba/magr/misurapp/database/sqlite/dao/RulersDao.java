package it.uniba.magr.misurapp.database.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import it.uniba.magr.misurapp.database.sqlite.bean.Ruler;

/**
 * The Data access object (Dao) of ruler measurements.
 */
@Dao
public interface RulersDao {

    /**
     * @param measureId The foreign key of the measure.
     * @return The ruler info.
     */
    @Query("SELECT * FROM Rulers WHERE measure_id=:measureId")
    Ruler getRuler(int measureId);

    /**
     * @param ruler a not null ruler instance.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRuler(Ruler ruler);

    /**
     * @param rulers a not null array of ruler instances.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRulers(Ruler... rulers);

}