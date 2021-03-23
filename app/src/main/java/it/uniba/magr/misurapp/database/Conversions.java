package it.uniba.magr.misurapp.database;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.Locale;

import it.uniba.magr.misurapp.database.bean.Type;

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

    @TypeConverter
    public static Type stringToType(int typeOrdinal) {
        return Type.values()[typeOrdinal];
    }

    @TypeConverter
    public static int typeToInt(Type type) {
        return type.ordinal();
    }

    private Conversions() {
        throw new IllegalStateException("This is a static class");
    }

}
