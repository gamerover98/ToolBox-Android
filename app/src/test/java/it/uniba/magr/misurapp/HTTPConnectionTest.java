package it.uniba.magr.misurapp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.junit.jupiter.api.Test;

import it.uniba.magr.misurapp.http.HTTPConnection;

import static org.junit.jupiter.api.Assertions.*;

class HTTPConnectionTest {

    private static final Gson GSON = new Gson();

    private static final String ERROR   = "error";
    private static final String MESSAGE = "message";

    private static final int ERROR_4 = 4;

    @Test
    void testConnection() {

        HTTPConnection connection = new HTTPConnection();
        RequestBody emptyBody = connection.createEmptyBody();
        Request.Builder builder = connection.createPostBuilder(emptyBody);

        assertDoesNotThrow(() -> {

            String response = connection.sendRequest(builder);
            JsonObject errorResponse = JsonParser.parseString(response).getAsJsonObject();

            assertTrue(errorResponse.has(ERROR));
            assertTrue(errorResponse.has(MESSAGE));

            int errorCode = errorResponse.get(ERROR).getAsInt();
            assertEquals(ERROR_4, errorCode);

        });

    }

}