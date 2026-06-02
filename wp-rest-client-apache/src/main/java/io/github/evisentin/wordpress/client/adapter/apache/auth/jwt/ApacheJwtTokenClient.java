package io.github.evisentin.wordpress.client.adapter.apache.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthException;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.jwt.JwtResponse;
import io.github.evisentin.wordpress.client.domain.auth.jwt.JwtTokenClient;
import lombok.NonNull;
import lombok.val;
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
        this.authHttpClient = authHttpClient;
        this.mapper = new ObjectMapper();
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
                val statusCode = response.getCode();
                val body = response.getEntity() == null
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
