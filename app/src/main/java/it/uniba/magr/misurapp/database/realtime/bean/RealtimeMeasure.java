package it.uniba.magr.misurapp.database.realtime.bean;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class RealtimeMeasure {

    /**
     * The sqlite measure id.
     * This is useful to grant the correct deleting.
     */
    private int measureId;

    /**
     * The title.
     */
    @NonNull
    protected String title;

    /**
     * The description.
     */
    @NonNull
    protected String description;

    /**
     * The start date.
     */
    @NonNull
    protected Date startDate;

    protected RealtimeMeasure(int measureId, @NotNull String title,
                              @NotNull String description, @NotNull Date startDate) {

        this.measureId   = measureId;
        this.title       = title;
        this.description = description;
        this.startDate   = startDate;

    }

    //
    // GETTERS
    //

    public int getMeasureId() {
        return this.measureId;
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

    //
    // SETTERS
    //

    public void setMeasureId(int measureId) {
        this.measureId = measureId;
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

}