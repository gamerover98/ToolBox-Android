package it.uniba.magr.misurapp.http.request.user;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import it.uniba.magr.misurapp.http.request.Packet;
import it.uniba.magr.misurapp.http.request.exception.MalformedRequestException;
import it.uniba.magr.misurapp.http.request.exception.MalformedResponseException;
import lombok.Getter;
import lombok.Setter;

/**
 * The add/update user packet to manage
 * the user info into the database.
 */
public class UpdateUserPacket implements Packet {

    /**
     * The packet name.
     */
    private static final String REQUEST_NAME = "updateuser";

    private static final String USER_ID_PROPERTY = "user_id";
    private static final String NAME_PROPERTY    = "name";
    private static final String SURNAME_PROPERTY = "surname";
    private static final String RESULT_PROPERTY  = "result";

    /**
     * Gets or sets the unique user id provided by Firebase.
     */
    @Getter @Setter @Nullable
    private String userID;

    /**
     * Gets or sets the real person name.
     */
    @Getter @Setter @Nullable
    private String name;

    /**
     * Gets or sets the real person surname.
     */
    @Getter @Setter @Nullable
    private String surname;

    /**
     * Gets the result after the receiving packet.
     */
    @Getter @Nullable
    private Result result;

    public UpdateUserPacket() {
        // nothing to do
    }

    @NotNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Map<String, Object> send() throws MalformedRequestException {

        checkNotNull(USER_ID_PROPERTY, userID);
        checkNotNull(NAME_PROPERTY,    name);
        checkNotNull(SURNAME_PROPERTY, surname);

        Map<String, Object> request = new HashMap<>();

        request.put(USER_ID_PROPERTY, userID);
        request.put(NAME_PROPERTY,    name);
        request.put(SURNAME_PROPERTY, surname);

        return request;

    }

    @Override
    public void receive(@NotNull JsonObject response) throws MalformedResponseException {

        if (!response.has(RESULT_PROPERTY)) {
            throw new MalformedResponseException(this, response,
                    "Missing " + RESULT_PROPERTY + " property");
        }

        String receivedResult = response.get(RESULT_PROPERTY).getAsString();

        try {

            result = Result.valueOf(receivedResult.toUpperCase());

        } catch (IllegalArgumentException iaEx) {

            throw new MalformedResponseException(this, response,
                    "Wrong " + RESULT_PROPERTY + " property." +
                            " It can be " + Arrays.toString(Result.values())
                            + " but it is " + receivedResult);

        }

    }

    @NotNull
    @Override
    public String getRequestName() {
        return REQUEST_NAME;
    }

    /**
     * The add/update request enumeration class.
     */
    public enum Result {

        /**
         * This result means that the user has been
         * added to the server database.
         */
        CREATED,

        /**
         * This result means that the user has been
         * updated to the server database.
         */
        UPDATED

    }

}
