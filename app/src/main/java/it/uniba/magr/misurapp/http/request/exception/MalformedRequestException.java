package it.uniba.magr.misurapp.http.request.exception;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.http.request.Packet;
import lombok.Getter;

/**
 * Used when the required sending packet is wrong.
 */
public class MalformedRequestException extends PacketException {

    @Getter @NotNull
    private final transient Packet packet;

    public MalformedRequestException(@NotNull Packet packet,
                                     @NotNull String message) {

        super("Malformed packet " + packet.getClass().getName() + "."
                + (message.isEmpty() ? "" : " " + message));

        this.packet = packet;

    }

}
