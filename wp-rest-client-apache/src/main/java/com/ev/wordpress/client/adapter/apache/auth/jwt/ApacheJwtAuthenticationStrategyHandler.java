package com.ev.wordpress.client.adapter.apache.auth.jwt;

import com.ev.wordpress.client.adapter.apache.auth.ApacheAuthenticationStrategyHandler;
import com.ev.wordpress.client.domain.auth.WpAuthException;
import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.ev.wordpress.client.adapter.apache.auth.jwt.TokenUtils.isExpired;
import static com.ev.wordpress.client.adapter.apache.auth.jwt.TokenUtils.resolveExpiration;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.hc.core5.http.HttpHeaders.ACCEPT;
import static org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;

public class ApacheJwtAuthenticationStrategyHandler implements ApacheAuthenticationStrategyHandler<WpJwtAuthenticationStrategy> {

    private final Object lock = new Object();
    private final ObjectMapper mapper = new ObjectMapper();
    private final CloseableHttpClient authHttpClient;

    private String jwtToken = null;
    private Instant expiresIn = null;

    public ApacheJwtAuthenticationStrategyHandler(final @NonNull CloseableHttpClient authHttpClient) {
        this.authHttpClient = authHttpClient;
    }

    @Override
    public String authenticateTyped(final @NonNull WpJwtAuthenticationStrategy strategy) {

        if (allNotNull(jwtToken, expiresIn) && !isExpired(expiresIn))
            return "Bearer " + jwtToken;

        synchronized (lock) {
            HttpPost request = new HttpPost(strategy.jwtTokenUrl());
            request.addHeader(ACCEPT, "application/json");
            request.setHeader(CONTENT_TYPE, "application/x-www-form-urlencoded");

            request.setEntity(new UrlEncodedFormEntity(List.of(
                    new BasicNameValuePair("username", strategy.username()),
                    new BasicNameValuePair("password", strategy.password())
            )));

            try {
                JwtResponse jwtResponse = authHttpClient.execute(request, response -> {
                    final int statusCode = response.getCode();
                    final String body = response.getEntity() == null ? "" : EntityUtils.toString(response.getEntity());

                    if (statusCode < 200 || statusCode >= 300) {
                        throw new WpAuthException("Unexpected code " + statusCode + ": " + body);
                    }

                    return mapper.readValue(body, JwtResponse.class);
                });

                this.jwtToken = jwtResponse.getJwtToken();
                this.expiresIn = resolveExpiration(jwtResponse.getExpiresIn(), jwtResponse.getIat());

                return "Bearer " + jwtToken;
            } catch (IOException e) {
                throw new WpAuthException("Failed to authenticate using JWT", e);
            }
        }
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
