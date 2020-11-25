package it.uniba.magr.misurapp.http.request.exception;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.http.request.Packet;
import lombok.Getter;

/**
 * Used if the response provided from the server is wrong.
 */
public class MalformedResponseException extends PacketException {

    /**
     * Gets the indicted packet.
     */
    @Getter @NotNull
    private final transient Packet packet;

    /**
     * Gets the json server response.
     */
    @Getter @NotNull
    private final transient JsonObject packetContent;

    public MalformedResponseException(@NotNull Packet packet,
                                      @NotNull JsonObject packetContent,
                                      @NotNull String message) {

        super("Malformed packet " + packet.getClass().getName()
                + " Response: " + packetContent.toString() + "."
                + (message.isEmpty() ? "" : " " + message));

        this.packet = packet;
        this.packetContent = packetContent;

    }

}
