package it.uniba.magr.misurapp.database.sqlite;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.uniba.magr.misurapp.database.sqlite.bean.Magnetometer;
import it.uniba.magr.misurapp.database.sqlite.bean.Measure;
import it.uniba.magr.misurapp.database.sqlite.bean.Ruler;
import it.uniba.magr.misurapp.database.sqlite.bean.Barometer;
import it.uniba.magr.misurapp.database.sqlite.dao.BarometersDao;
import it.uniba.magr.misurapp.database.sqlite.dao.MagnetometersDao;
import it.uniba.magr.misurapp.database.sqlite.dao.MeasurementsDao;
import it.uniba.magr.misurapp.database.sqlite.dao.RulersDao;

/**
 * The main database class to handle and perform DB instructions.
 */
@Database(entities = {Measure.class, Ruler.class, Barometer.class, Magnetometer.class},
        version = 1, exportSchema = false)
@TypeConverters({Conversions.class})
public abstract class SqliteManager extends RoomDatabase {

    /**
     * @return The instance of the MeasurementDao class.
     */
    public abstract MeasurementsDao measurementsDao();

    /**
     * @return The instance of the RulersDao class.
     */
    public abstract RulersDao rulersDao();

    /**
     * @return The instance of the BarometersDao class.
     */
    public abstract BarometersDao barometersDao();

    /**
     * @return The instance of the MagnetometersDao class.
     */
    public abstract MagnetometersDao magnetometersDao();

}
