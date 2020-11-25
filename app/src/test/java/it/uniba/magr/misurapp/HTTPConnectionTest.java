package it.uniba.magr.misurapp;

import com.google.gson.JsonObject;

import org.junit.jupiter.api.Test;

import it.uniba.magr.misurapp.http.HTTPConnection;
import it.uniba.magr.misurapp.http.request.user.UpdateUserPacket;
import it.uniba.magr.misurapp.http.request.exception.MalformedRequestException;

import static org.junit.jupiter.api.Assertions.*;

class HTTPConnectionTest {

    private static final String ERROR   = "error";
    private static final String MESSAGE = "message";

    private static final int ERROR_4 = 4;

    private static final String TEST_USER_ID      = "test:123456789";
    private static final String TEST_USER_NAME    = "Mario";
    private static final String TEST_USER_SURNAME = "Rossi";

    @Test
    void testConnection() {

        HTTPConnection connection = new HTTPConnection();

        assertDoesNotThrow(() -> {

            JsonObject errorResponse = connection.sendEmptyRequest();

            assertTrue(errorResponse.has(ERROR));
            assertTrue(errorResponse.has(MESSAGE));

            int errorCode = errorResponse.get(ERROR).getAsInt();
            assertEquals(ERROR_4, errorCode);

        });

    }

    @Test
    void testUpdateUserMalformedRequestPacket() {

        HTTPConnection connection = new HTTPConnection();
        UpdateUserPacket packet = new UpdateUserPacket();

        assertThrows(MalformedRequestException.class, () -> connection.sendRequest(packet));

    }

    @Test
    void testUpdateUserReceivePacket() {

        HTTPConnection connection = new HTTPConnection();
        UpdateUserPacket packet = new UpdateUserPacket();

        packet.setUserID(TEST_USER_ID);
        packet.setName(TEST_USER_NAME);
        packet.setSurname(TEST_USER_SURNAME);

        assertDoesNotThrow(() -> {

            connection.sendRequest(packet);

            UpdateUserPacket.Result result = packet.getResult();
            assertNotNull(result);

        });

    }

}