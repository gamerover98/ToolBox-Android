package it.uniba.magr.misurapp.database;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * The room's database class conversions.
 */
public final class Conversions {

    /**
     * @param value a date in long type.
     * @return The argument date to a date instance.
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * @param date a Date instance.
     * @return The argument date into a long value.
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    private Conversions() {
        throw new IllegalStateException("This is a static class");
    }

}
