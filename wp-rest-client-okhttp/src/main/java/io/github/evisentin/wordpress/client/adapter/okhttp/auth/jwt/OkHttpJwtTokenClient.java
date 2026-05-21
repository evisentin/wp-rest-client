package io.github.evisentin.wordpress.client.adapter.okhttp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthException;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.jwt.JwtResponse;
import io.github.evisentin.wordpress.client.domain.auth.jwt.JwtTokenClient;
import lombok.NonNull;
import okhttp3.*;

import java.io.IOException;

import static io.github.evisentin.wordpress.client.adapter.okhttp.http.HttpHeaders.ACCEPT;
import static io.github.evisentin.wordpress.client.adapter.okhttp.http.MimeTypes.APPLICATION_JSON;

public class OkHttpJwtTokenClient implements JwtTokenClient {

    private final OkHttpClient authHttpClient;
    private final ObjectMapper mapper;

    public OkHttpJwtTokenClient(final @NonNull OkHttpClient authHttpClient) {
        this(authHttpClient, new ObjectMapper());
    }

    public OkHttpJwtTokenClient(final @NonNull OkHttpClient authHttpClient,
                                final @NonNull ObjectMapper mapper) {
        this.authHttpClient = authHttpClient;
        this.mapper = mapper;
    }

    @Override
    public JwtResponse fetchToken(final @NonNull WpJwtAuthenticationStrategy strategy) {

        RequestBody formBody = new FormBody.Builder()
                .add("username", strategy.username())
                .add("password", strategy.password())
                .build();

        Request request = new Request.Builder()
                .url(strategy.jwtTokenUrl())
                .addHeader(ACCEPT, APPLICATION_JSON)
                .post(formBody)
                .build();

        try (Response response = authHttpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                String body = response.body() != null ? response.body().string() : "";
                throw new WpAuthException(
                        "Failed to fetch JWT token. HTTP " + response.code() + ": " + body
                );
            }

            String body = response.body() != null ? response.body().string() : "";

            return mapper.readValue(body, JwtResponse.class);
        } catch (IOException e) {
            throw new WpAuthException("Failed to fetch JWT token", e);
        }
    }
}
