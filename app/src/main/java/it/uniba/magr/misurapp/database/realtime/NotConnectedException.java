package it.uniba.magr.misurapp.database.realtime;

/**
 * The not connected to internet exception.
 * Used to prevent missing operations on the firebase database.
 */
public class NotConnectedException extends Exception {

    public NotConnectedException() {
        super("Not connected to an internet connection");
    }

}