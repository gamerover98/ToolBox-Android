package it.uniba.magr.misurapp.database.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import it.uniba.magr.misurapp.database.Conversions;
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
     * <p>The auto-increment default value is 1.</p>
     */
    @PrimaryKey(autoGenerate = true)
    private int id = 1;

    /**
     * The firebase user's token.
     *
     * <p>
     *     If the value is empty, it will match the guest
     *     user (not logged-in in firebase).
     * </p>
     */
    @NonNull
    @ColumnInfo(name = "user_token", defaultValue = "")
    private String userToken = "";

    /**
     * The title.
     */
    @NonNull
    @ColumnInfo(name = "title", defaultValue = "")
    private String title = "";

    /**
     * The description.
     */
    @NonNull
    @ColumnInfo(name = "description", defaultValue = "")
    private String description = "";

    /**
     * The start date.
     */
    @NonNull
    @ColumnInfo(name = "start_date")
    private Date startDate = new Date(System.currentTimeMillis());

    /**
     * The end date.
     */
    @NonNull
    @ColumnInfo(name = "end_date")
    private Date endDate = new Date(System.currentTimeMillis());

    public int getId() {
        return this.id;
    }

    @NotNull
    public String getUserToken() {
        return this.userToken;
    }

    @NotNull
    public String getTitle() {
        return this.title;
    }

    @NotNull
    public String getDescription() {
        return this.description;
    }

    @NotNull
    public Date getStartDate() {
        return this.startDate;
    }

    @NotNull
    public Date getEndDate() {
        return this.endDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserToken(@NonNull String userToken) {
        this.userToken = userToken;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setStartDate(@NonNull Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(@NonNull Date endDate) {
        this.endDate = endDate;
    }

}