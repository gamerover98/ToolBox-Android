package it.uniba.magr.misurapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * The main database class to handle and perform DB instructions.
 */
@Database(entities = {Measure.class}, version = 1)
@TypeConverters({Conversions.class})
public abstract class DatabaseManager extends RoomDatabase {

    /**
     * @return The DAO of the Measurements class.
     */
    public abstract MeasurementsDao measurementsDao();

}
