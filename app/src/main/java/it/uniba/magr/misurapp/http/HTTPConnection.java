package it.uniba.magr.misurapp.http;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * Provides to send and receive the contents from the remote MisurApp server.
 */
public class HTTPConnection {

    private static final Gson GSON = new Gson();

    private static final String JSON_MIME = "application/json; charset=utf-8";
    private static final MediaType JSON_TYPE = MediaType.parse(JSON_MIME);

    private static final String SERVER_HTTP_ADDRESS = "146.59.229.43";
    private static final int    SERVER_HTTP_PORT    = 80;

    private static final String SERVER_HTTP_URL =
            "http://" + SERVER_HTTP_ADDRESS + ":" + SERVER_HTTP_PORT;

    /**
     * The HTTP client.
     */
    @Getter @NotNull
    private final OkHttpClient client;

    public HTTPConnection() {
        this.client = new OkHttpClient();
    }

    /**
     * Create a POST builder for request.
     *
     * @param postBody A not null instance of the post body.
     * @return The not null POST builder request.
     */
    @NotNull
    @SneakyThrows(MalformedURLException.class)
    public Request.Builder createPostBuilder(@NotNull RequestBody postBody) {
        return new Request.Builder().url(new URL(SERVER_HTTP_URL)).post(postBody);
    }

    /**
     * Create a JSON body for your HTTP packet.
     *
     * @param bodyContent An object that contains serializable Type fields.
     *                    Null if you want an empty body.
     * @return The not null body.
     */
    @NotNull
    public RequestBody createJsonBody(@Nullable Object bodyContent) {

        if (bodyContent == null) {
            return RequestBody.create(null, new byte[] {});
        }

        String jsonContent = GSON.toJson(bodyContent);
        return RequestBody.create(JSON_TYPE, jsonContent);

    }

    /**
     * Crea an empty HTTP packet.
     * @return The not null body.
     */
    @NotNull
    public RequestBody createEmptyBody() {
        return createJsonBody(null);
    }

    /**
     * Send the built request to send at the HTTP server.
     * @param builder A not null instance of a packet builder.
     * @return The string (JSON) that contains the response.
     * @throws IOException If the server is not available.
     */
    @NotNull
    public String sendRequest(@NotNull Request.Builder builder) throws IOException {

        Request request = builder.build();
        Response response = client.newCall(request).execute();

        return response.body().string();

    }

}
