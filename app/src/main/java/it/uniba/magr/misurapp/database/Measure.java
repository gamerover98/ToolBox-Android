package it.uniba.magr.misurapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.ToString;

/**
 * A single entry (row's table) of the Measurements table.
 * In this database version will be:
 * - ID:          PRIMARY KEY AUTO_INCREMENT
 * - user_token:  TEXT NOT NULL
 * - title:       TEXT NOT NULL
 * - description: TEXT NOT NULL
 * - date:        LONG NOT NULL
 *
 * <p>
 *     The date is long due to room's database cannot manage objects references.
 *     Check the {@link Conversions} class to know how the room's database handle
 *     the type conversions.
 * </p>
 */
@ToString
@Entity(tableName = "Measurements")
public class Measure {

    /**
     * A common integer ID that will be auto incremented by default.
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * The firebase user's token.
     */
    @NonNull
    @ColumnInfo(name = "user_token", defaultValue = "")
    public String userToken = "";

    /**
     * The title.
     */
    @NonNull
    @ColumnInfo(name = "title", defaultValue = "")
    public String title = "";

    /**
     * The description.
     */
    @NonNull
    @ColumnInfo(name = "description", defaultValue = "")
    public String description = "";

    /**
     * The date.
     */
    @NonNull
    @ColumnInfo(name = "date")
    public Date date = new Date(System.currentTimeMillis());

}