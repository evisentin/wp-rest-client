package io.github.evisentin.wordpress.client.domain.exception;

public class ApiUrlNotFoundException extends RuntimeException {

    public ApiUrlNotFoundException(final String baseUrl) {
        super("Unable to discover WordPress API URL from: " + baseUrl);
    }
}
