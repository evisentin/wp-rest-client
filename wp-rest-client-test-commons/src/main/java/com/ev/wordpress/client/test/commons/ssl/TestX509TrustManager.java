package com.ev.wordpress.client.test.commons.ssl;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Insecure {@link X509TrustManager} implementation for testing purposes.
 *
 * <p>This trust manager performs no validation of client or server
 * certificates, effectively trusting all certificates presented during SSL/TLS handshakes.</p>
 *
 * <p>It is intended solely for use in test environments where certificate
 * validation would otherwise fail (e.g. self-signed certificates in containerized setups).</p>
 *
 * <p><strong>Security warning:</strong> This implementation disables all
 * certificate validation and must never be used in production code.</p>
 *
 * <h2>Behavior</h2>
 * <ul>
 *   <li>{@link #checkClientTrusted(X509Certificate[], String)} - no-op</li>
 *   <li>{@link #checkServerTrusted(X509Certificate[], String)} - no-op</li>
 *   <li>{@link #getAcceptedIssuers()} - returns an empty array</li>
 * </ul>
 */
public class TestX509TrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
