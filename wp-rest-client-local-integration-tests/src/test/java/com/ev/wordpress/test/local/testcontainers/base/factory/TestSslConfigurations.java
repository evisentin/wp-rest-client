package com.ev.wordpress.test.local.testcontainers.base.factory;

import com.ev.wordpress.client.domain.configuration.SslConfiguration;
import lombok.SneakyThrows;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

/**
 * Utility class providing SSL configurations for testing purposes.
 *
 * <p>This class exposes factory methods that create intentionally insecure
 * {@link SslConfiguration} instances suitable for integration tests where HTTPS endpoints use self-signed or
 * dynamically generated certificates.</p>
 *
 * <p>All configurations produced by this class:</p>
 * <ul>
 *   <li>Trust all X.509 certificates</li>
 *   <li>Disable hostname verification</li>
 * </ul>
 *
 * <p><strong>Warning:</strong> These configurations must never be used in
 * production environments as they disable all SSL security guarantees.</p>
 *
 * <h2>Client-specific configurations</h2>
 * <ul>
 *   <li>{@link #insecureForApache()} - suitable for Apache HttpClient-based clients</li>
 *   <li>{@link #insecureForOkHttp()} - suitable for OkHttp-based clients</li>
 * </ul>
 *
 * <h2>Implementation details</h2>
 * <p>Uses {@link TestX509TrustManager} to bypass certificate validation and,
 * for OkHttp, initializes a custom {@link SSLContext} with this trust manager.</p>
 *
 * @see SslConfiguration
 * @see TestX509TrustManager
 */
public final class TestSslConfigurations {

    private TestSslConfigurations() {}

    public static SslConfiguration insecureForApache() {
        X509TrustManager trustManager = new TestX509TrustManager();

        return SslConfiguration.builder()
                               .trustManager(trustManager)
                               .hostnameVerifier((h, s) -> true)
                               .build();
    }

    @SneakyThrows
    public static SslConfiguration insecureForOkHttp() {
        X509TrustManager trustManager = new TestX509TrustManager();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        return SslConfiguration.builder()
                               .sslSocketFactory(sslContext.getSocketFactory())
                               .trustManager(trustManager)
                               .hostnameVerifier((h, s) -> true)
                               .build();
    }
}
