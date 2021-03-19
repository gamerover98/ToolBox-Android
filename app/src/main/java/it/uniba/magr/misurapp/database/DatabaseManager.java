package it.uniba.magr.misurapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.bean.Ruler;
import it.uniba.magr.misurapp.database.dao.MeasurementsDao;
import it.uniba.magr.misurapp.database.dao.RulersDao;

/**
 * The main database class to handle and perform DB instructions.
 */
@Database(entities = {Measure.class, Ruler.class}, version = 1, exportSchema = false)
@TypeConverters({Conversions.class})
public abstract class DatabaseManager extends RoomDatabase {

    /**
     * @return The instance of the MeasurementDao class.
     */
    public abstract MeasurementsDao measurementsDao();

    /**
     * @return The instance of the RulersDao class.
     */
    public abstract RulersDao rulersDao();

}
