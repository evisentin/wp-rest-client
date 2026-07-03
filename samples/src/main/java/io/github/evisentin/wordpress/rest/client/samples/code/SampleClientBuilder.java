package io.github.evisentin.wordpress.rest.client.samples.code;

import io.github.evisentin.wordpress.rest.client.adapter.apache.ApacheWpRestClientBuilder;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.OkHttpWpRestClientBuilder;
import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.samples.apache.SampleApacheHttpRequestInterceptor;
import io.github.evisentin.wordpress.rest.client.samples.apache.SampleApacheHttpResponseInterceptor;
import io.github.evisentin.wordpress.rest.client.samples.okhttp.SampleOkHttpRequestInterceptor;
import io.github.evisentin.wordpress.rest.client.samples.okhttp.SampleOkHttpResponseInterceptor;

/**
 * Utility class used by the sample applications to create a configured {@link WpRestClient}.
 * <p>
 * The builder supports all combinations of the available HTTP client implementations and authentication mechanisms:
 * <ul>
 *     <li>Apache HttpClient with Basic Authentication</li>
 *     <li>Apache HttpClient with JWT Authentication</li>
 *     <li>OkHttp with Basic Authentication</li>
 *     <li>OkHttp with JWT Authentication</li>
 * </ul>
 * <p>
 * The returned client is preconfigured with sample request and response interceptors to demonstrate how interceptors
 * can be registered.
 * <p>
 * This class is intended solely for the sample applications and is not required when integrating the library into your
 * own project.
 */
public class SampleClientBuilder {

    static final String BASE_URL = "http://localhost:8080";
    static final String JWT_TOKEN_ENDPOINT = "/api/v1/token";
    static final String USER_NAME = "admin";
    static final String PASSWORD = "admin";

    /**
     * Creates a fully configured {@link WpRestClient} using the specified authentication mechanism and HTTP client
     * implementation.
     *
     * @param authenticationType
     *         the authentication mechanism to use
     * @param implementation
     *         the HTTP client implementation to use
     *
     * @return a configured {@link WpRestClient}
     */
    public static WpRestClient buildClientFor(final AuthenticationType authenticationType,
                                              final Implementation implementation) {

        return switch (implementation) {
            case APACHE -> switch (authenticationType) {
                case BASIC_AUTH -> ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USER_NAME, PASSWORD)
                        .withInterceptor(new SampleApacheHttpRequestInterceptor())
                        .withInterceptor(new SampleApacheHttpResponseInterceptor())
                        .build();

                case JWT -> ApacheWpRestClientBuilder
                        .jwtAuthentication(BASE_URL, USER_NAME, PASSWORD, JWT_TOKEN_ENDPOINT)
                        .withInterceptor(new SampleApacheHttpRequestInterceptor())
                        .withInterceptor(new SampleApacheHttpResponseInterceptor())
                        .build();
            };

            case OK_HTTP -> switch (authenticationType) {
                case BASIC_AUTH -> OkHttpWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USER_NAME, PASSWORD)
                        .withInterceptor(new SampleOkHttpRequestInterceptor())
                        .withInterceptor(new SampleOkHttpResponseInterceptor())
                        .build();

                case JWT -> OkHttpWpRestClientBuilder
                        .jwtAuthentication(BASE_URL, USER_NAME, PASSWORD, JWT_TOKEN_ENDPOINT)
                        .withInterceptor(new SampleOkHttpRequestInterceptor())
                        .withInterceptor(new SampleOkHttpResponseInterceptor())
                        .build();
            };
        };
    }

    public enum AuthenticationType {
        BASIC_AUTH, JWT
    }

    public enum Implementation {
        APACHE, OK_HTTP
    }
}
