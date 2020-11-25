package it.uniba.magr.misurapp.http.request.exception;

/**
 * Generic packet exception.
 */
@SuppressWarnings("unused") // unused constructor
public class PacketException extends Exception {

    public PacketException(String message) {
        super(message);
    }

    public PacketException(String message, Throwable cause) {
        super(message, cause);
    }

}
