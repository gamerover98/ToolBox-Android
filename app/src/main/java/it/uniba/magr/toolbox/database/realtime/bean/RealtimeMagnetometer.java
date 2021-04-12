package it.uniba.magr.toolbox.database.realtime.bean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.ToString;

@ToString
public class RealtimeMagnetometer extends RealtimeMeasure {

    /**
     * Due to this exception:
     *
     * com.google.firebase.database.DatabaseException: Serializing Arrays
     * is not supported, please use Lists instead
     *
     * This cannot be an array but a simple list.
     *
     */
    protected List<Integer> seconds;

    /**
     * Due to this exception:
     *
     * com.google.firebase.database.DatabaseException: Serializing Arrays
     * is not supported, please use Lists instead
     *
     * This cannot be an array but a simple list.
     *
     */
    protected List<Float> values;

    @SuppressWarnings("unused") // used with reflection by firebase.
    public RealtimeMagnetometer() {

        super(0, "", "", new Date());

        this.seconds = new ArrayList<>();
        this.values  = new ArrayList<>();

    }

    @NotNull
    public List<Integer> getSeconds() {
        return this.seconds;
    }

    @NotNull
    public List<Float> getValues() {
        return this.values;
    }

    public void setSeconds(@NotNull List<Integer> seconds) {
        this.seconds = seconds;
    }

    public void setValues(@NotNull List<Float> values) {
        this.values = values;
    }

}