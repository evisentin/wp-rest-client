package io.github.evisentin.wordpress.client.domain.configuration;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;

@Value
@Builder
public class TimeoutConfiguration {

    /**
     * Time to establish the connection.
     */
    Duration connectTimeout;

    /**
     * Time waiting for data (socket read).
     */
    Duration readTimeout;

    /**
     * Time waiting to send data.
     */
    Duration writeTimeout;

    /**
     * Time waiting for a connection from the pool. (Apache-specific, optional for OkHttp)
     */
    Duration connectionRequestTimeout;

    /**
     * Optional overall call timeout (OkHttp supports it).
     */
    Duration callTimeout;
}
