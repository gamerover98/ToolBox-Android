package it.uniba.magr.misurapp.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import it.uniba.magr.misurapp.http.request.Packet;
import it.uniba.magr.misurapp.http.request.exception.PacketException;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * Provides to send and receive the contents from the remote MisurApp server.
 */
public class HTTPConnection {

    /**
     * The currently IP address of the HTTP remote server.
     */
    private static final String SERVER_HTTP_ADDRESS = "146.59.229.43";

    /**
     * The currently HTTP remote server PORT.
     */
    private static final int    SERVER_HTTP_PORT    = 80;

    /**
     * The HTTP url (without SSL/TLS encryption)
     */
    private static final String SERVER_HTTP_URL =
            "http://" + SERVER_HTTP_ADDRESS + ":" + SERVER_HTTP_PORT;

    /**
     * The required request packet body key that will be associated with
     * the required operation name.
     */
    private static final String REQUEST_PROPERTY = "request";

    /**
     * Gets the not null instance of the OkHTTP client.
     */
    @Getter @NotNull
    private final OkHttpClient client;

    /**
     * Initialize the HTTP connection.
     */
    public HTTPConnection() {
        this.client = new OkHttpClient();
    }

    /**
     * Send a packet to the HTTP server.
     *
     * @param packet the not null instance of a packet implementation instance.
     * @throws IOException If the server is not available.
     * @throws PacketException If the packet is incorrect.
     */
    @SneakyThrows(MalformedURLException.class) // the URL should be correct
    public void sendRequest(@NotNull Packet packet) throws IOException, PacketException {

        Map<String, Object> packetMap = packet.send();
        packetMap.putIfAbsent(REQUEST_PROPERTY, packet.getRequestName());

        MultipartBuilder multipartBuilder = new MultipartBuilder();

        multipartBuilder.type(MultipartBuilder.FORM);
        packetMap.forEach((key, value) -> multipartBuilder.addFormDataPart(key, value.toString()));

        Request.Builder builder = new Request.Builder()
                .url(new URL(SERVER_HTTP_URL)).post(multipartBuilder.build());

        Request request = builder.build();
        Response response = client.newCall(request).execute();

        JsonObject jsonObject = toJsonObject(response);
        packet.receive(jsonObject);

    }

    /**
     * Send an empty POST request to the server.
     *
     * @throws IOException If the server is not available.
     * @return The json object response.
     */
    @SneakyThrows(MalformedURLException.class) // the URL should be correct
    public JsonObject sendEmptyRequest() throws IOException {

        RequestBody emptyRequest = RequestBody.create(null, new byte[] {});
        Request.Builder builder = new Request.Builder()
                .url(new URL(SERVER_HTTP_URL)).post(emptyRequest);

        Request request = builder.build();
        Response response = client.newCall(request).execute();

        return toJsonObject(response);

    }

    /**
     * From Response to JsonObject.
     *
     * @param response The HTTP request response.
     * @return A not null instance of JsonObject that contains response info.
     * @throws IOException If the server does not provide the response.
     */
    @NotNull
    private JsonObject toJsonObject(@NotNull Response response) throws IOException {

        String stringResponse = response.body().string();
        return JsonParser.parseString(stringResponse).getAsJsonObject();

    }

}
