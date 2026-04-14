package com.ev.wordpress.client.adapter.okhttp;

import lombok.Builder;
import lombok.Value;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Encapsulates SSL/TLS configuration to be applied to the underlying HTTP client.
 * <p>
 * This class allows advanced users to customize the SSL behavior of the client, such as:
 * <ul>
 *     <li>Providing a custom {@link SSLSocketFactory} (e.g. for mutual TLS or custom trust stores)</li>
 *     <li>Defining a specific {@link X509TrustManager}</li>
 *     <li>Overriding hostname verification via {@link HostnameVerifier}</li>
 * </ul>
 *
 * <p>
 * When provided to the client, all non-null components will be applied to the
 * underlying HTTP client configuration.
 *
 * <p><b>Security note:</b><br>
 * Misconfiguration of SSL components (e.g. trusting all certificates or disabling
 * hostname verification) can expose the application to man-in-the-middle attacks.
 * Such configurations should only be used in controlled environments (e.g. testing).
 *
 * <p>
 * Instances of this class are immutable and can be safely reused.
 */
@Value
@Builder
public class SslConfiguration {
    SSLSocketFactory sslSocketFactory;
    X509TrustManager trustManager;
    HostnameVerifier hostnameVerifier;
}
