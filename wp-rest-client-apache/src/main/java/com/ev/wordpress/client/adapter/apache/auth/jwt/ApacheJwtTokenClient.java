package com.ev.wordpress.client.adapter.apache.auth.jwt;

import com.ev.wordpress.client.domain.auth.WpAuthException;
import com.ev.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.jwt.JwtResponse;
import com.ev.wordpress.client.domain.auth.jwt.JwtTokenClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;

import static org.apache.hc.core5.http.HttpHeaders.ACCEPT;
import static org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;

public class ApacheJwtTokenClient implements JwtTokenClient {

    private final CloseableHttpClient authHttpClient;
    private final ObjectMapper mapper;

    public ApacheJwtTokenClient(final @NonNull CloseableHttpClient authHttpClient) {
        this(authHttpClient, new ObjectMapper());
    }

    public ApacheJwtTokenClient(final @NonNull CloseableHttpClient authHttpClient,
                                final @NonNull ObjectMapper mapper) {
        this.authHttpClient = authHttpClient;
        this.mapper = mapper;
    }

    @Override
    public JwtResponse fetchToken(final @NonNull WpJwtAuthenticationStrategy strategy) {
        HttpPost request = new HttpPost(strategy.jwtTokenUrl());

        request.addHeader(ACCEPT, "application/json");
        request.setHeader(CONTENT_TYPE, "application/x-www-form-urlencoded");

        request.setEntity(new UrlEncodedFormEntity(List.of(
                new BasicNameValuePair("username", strategy.username()),
                new BasicNameValuePair("password", strategy.password())
        )));

        try {
            return authHttpClient.execute(request, response -> {
                int statusCode = response.getCode();
                String body = response.getEntity() == null
                        ? ""
                        : EntityUtils.toString(response.getEntity());

                if (statusCode < 200 || statusCode >= 300) {
                    throw new WpAuthException("Failed to fetch JWT token. HTTP " + statusCode + ": " + body);
                }

                return mapper.readValue(body, JwtResponse.class);
            });
        } catch (IOException e) {
            throw new WpAuthException("Failed to fetch JWT token", e);
        }
    }
}
