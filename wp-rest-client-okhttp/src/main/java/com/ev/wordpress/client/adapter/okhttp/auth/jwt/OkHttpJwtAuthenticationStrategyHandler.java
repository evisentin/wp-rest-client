package com.ev.wordpress.client.adapter.okhttp.auth.jwt;

import com.ev.wordpress.client.adapter.okhttp.auth.OkHttpAuthenticationStrategyHandler;
import com.ev.wordpress.client.domain.auth.WpAuthException;
import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;

import static com.ev.wordpress.client.adapter.okhttp.auth.jwt.TokenUtils.isExpired;
import static com.ev.wordpress.client.adapter.okhttp.auth.jwt.TokenUtils.resolveExpiration;
import static com.ev.wordpress.client.adapter.okhttp.constants.HttpHeaders.ACCEPT;
import static com.ev.wordpress.client.adapter.okhttp.constants.MimeTypes.APPLICATION_JSON;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;

public class OkHttpJwtAuthenticationStrategyHandler implements OkHttpAuthenticationStrategyHandler<WpJwtAuthenticationStrategy> {

    private final Object lock = new Object();
    private final ObjectMapper mapper = new ObjectMapper();
    private final OkHttpClient authHttpClient;

    private String jwtToken = null;
    private Instant expiresIn = null;

    public OkHttpJwtAuthenticationStrategyHandler(final @NonNull OkHttpClient authHttpClient) {
        this.authHttpClient = authHttpClient;
    }

    @Override
    public String authenticateTyped(final @NonNull WpJwtAuthenticationStrategy strategy) {

        if (allNotNull(jwtToken, expiresIn) && !isExpired(expiresIn))
            return "Bearer " + jwtToken;

        synchronized (lock) {
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
                    throw new WpAuthException("Unexpected code " + response);
                }

                final JwtResponse jwtResponse = mapper.readValue(response.body().string(), JwtResponse.class);

                this.jwtToken = jwtResponse.getJwtToken();
                this.expiresIn = resolveExpiration(jwtResponse.getExpiresIn(), jwtResponse.getIat());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return "Bearer " + jwtToken;
    }

    @Override
    public boolean canHandle(final @NonNull WpAuthenticationStrategy strategy) {
        return strategy instanceof WpJwtAuthenticationStrategy;
    }

    @Override
    public Class<WpJwtAuthenticationStrategy> supports() {
        return WpJwtAuthenticationStrategy.class;
    }
}
