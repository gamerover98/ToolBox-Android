package it.uniba.magr.misurapp.database.realtime.bean;

import java.util.Date;

import lombok.ToString;

@ToString
public class RealtimeBarometer extends RealtimeMeasure {

    /**
     * The pressure in hPa.
     */
    protected double pressure;

    @SuppressWarnings("unused") // used with reflection by firebase.
    public RealtimeBarometer() {

        super(0, "", "", new Date());
        this.pressure = 0;

    }

    public double getPressure() {
        return this.pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

}