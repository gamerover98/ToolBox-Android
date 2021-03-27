package it.uniba.magr.misurapp.database.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

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
@TypeConverters({Conversions.class})
public class Measure {

    /**
     * A common integer ID that will be auto incremented by default.
     * <p>The auto-increment default value is 1.</p>
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

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
     * The measure type like: ruler, magnetometer, etc.
     * The default value is UNKNOWN.
     */
    @NonNull
    @ColumnInfo(name = "type")
    private Type type = Type.UNKNOWN;

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
     * The order into the main interface.
     */
    @ColumnInfo(name = "card_order")
    private int cardOrder;

    //
    // GETTERS
    //

    public int getId() {
        return this.id;
    }

    @NotNull
    public String getUserToken() {
        return this.userToken;
    }

    @NonNull
    public Type getType() {
        return this.type;
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

    public int getCardOrder() {
        return this.cardOrder;
    }

    //
    // SETTERS
    //

    public void setId(int id) {
        this.id = id;
    }

    public void setUserToken(@NonNull String userToken) {
        this.userToken = userToken;
    }

    public void setType(@NonNull Type type) {
        this.type = type;
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

    public void setCardOrder(int cardOrder) {
        this.cardOrder = cardOrder;
    }

}