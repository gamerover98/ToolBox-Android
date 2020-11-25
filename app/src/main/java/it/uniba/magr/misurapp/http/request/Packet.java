package it.uniba.magr.misurapp.http.request;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import it.uniba.magr.misurapp.http.request.exception.MalformedRequestException;
import it.uniba.magr.misurapp.http.request.exception.MalformedResponseException;

/**
 * With this interface you can describe your sending and receiving packet from the server.
 * The sending content must be a map that contains key as property names and map
 * values as property values.
 *
 * An instance of its implementation will be managed
 * by {@link it.uniba.magr.misurapp.http.HTTPConnection}
 *
 */
public interface Packet {

    /**
     * Write the sending packet.
     *
     * @return The map that contains sending packet properties and values.
     * @throws MalformedRequestException If a required property is null.
     */
    @NotNull
    Map<String, Object> send() throws MalformedRequestException;

    /**
     * Handle the json response from the server.
     *
     * @param response The not null json response.
     * @throws MalformedResponseException If the response contains a property error.
     */
    void receive(@NotNull JsonObject response) throws MalformedResponseException;

    /**
     * @return The recognize name of the sending packet.
     */
    @NotNull
    String getRequestName();

    /**
     * Check if a property is null and if yes, throw a MalformedRequestException.
     *
     * @param property The not null property name.
     * @param obj The property value.
     * @throws MalformedRequestException If property value is null.
     */
    default void checkNotNull(@NotNull String property,
                              @Nullable Object obj) throws MalformedRequestException {

        if (obj == null) {
            throw new MalformedRequestException(this, property + " property cannot be null");
        }

    }

}
