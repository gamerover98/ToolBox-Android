package it.uniba.magr.toolbox.database.realtime.bean;

import java.util.Date;

import lombok.ToString;

@ToString
public class RealtimeRuler extends RealtimeMeasure {

    /**
     * The length in centimeters.
     */
    protected double length;

    @SuppressWarnings("unused") // used with reflection by firebase.
    public RealtimeRuler() {

        super(0, "", "", new Date());
        this.length = 0;

    }

    public double getLength() {
        return this.length;
    }

    public void setLength(double length) {
        this.length = length;
    }

}