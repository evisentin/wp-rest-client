package io.github.evisentin.wordpress.rest.client.adapter.okhttp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpAuthException;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.auth.jwt.JwtResponse;
import io.github.evisentin.wordpress.rest.client.domain.auth.jwt.JwtTokenClient;
import lombok.NonNull;
import okhttp3.*;

import java.io.IOException;
import java.net.URI;

import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.http.HttpHeaders.ACCEPT;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.http.MimeTypes.APPLICATION_JSON;

public class OkHttpJwtTokenClient implements JwtTokenClient {

    private final OkHttpClient authHttpClient;
    private final String apiUrl;
    private final ObjectMapper mapper;

    public OkHttpJwtTokenClient(final @NonNull OkHttpClient authHttpClient, final @NonNull String apiUrl) {
        this.authHttpClient = authHttpClient;
        this.apiUrl = apiUrl;
        this.mapper = new ObjectMapper();
    }

    @Override
    public JwtResponse fetchToken(final @NonNull WpJwtAuthenticationStrategy strategy) {

        RequestBody formBody = new FormBody.Builder()
                .add("username", strategy.username())
                .add("password", strategy.password())
                .build();

        Request request = new Request.Builder()
                .url(concatUrl(apiUrl, strategy.jwtTokenEndPoint()))
                .addHeader(ACCEPT, APPLICATION_JSON)
                .post(formBody)
                .build();

        try (Response response = authHttpClient.newCall(request).execute()) {

            String body = response.body().string();

            if (!response.isSuccessful())
                throw new WpAuthException("Failed to fetch JWT token. HTTP " + response.code() + ": " + body);

            return mapper.readValue(body, JwtResponse.class);
        } catch (IOException e) {
            throw new WpAuthException("Failed to fetch JWT token", e);
        }
    }

    private static String concatUrl(final String baseUrl, final String segment) {
        return URI.create(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/")
                  .resolve(segment.startsWith("/") ? segment.substring(1) : segment)
                  .toString();
    }
}
