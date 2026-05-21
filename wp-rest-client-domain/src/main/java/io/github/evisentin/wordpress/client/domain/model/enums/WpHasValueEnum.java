package io.github.evisentin.wordpress.client.domain.model.enums;

/**
 * Common contract for enums exposing a WordPress REST API value.
 */
public interface WpHasValueEnum {

    /**
     * Returns the API value associated with the enum constant.
     *
     * @return the serialized API value
     */
    String getValue();
}
