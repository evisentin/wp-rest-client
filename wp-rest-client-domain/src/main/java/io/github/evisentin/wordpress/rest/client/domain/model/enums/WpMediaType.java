package io.github.evisentin.wordpress.rest.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WpMediaType implements WpHasValueEnum {

    APPLICATION("application"),
    AUDIO("audio"),
    IMAGE("image"),
    TEXT("text"),
    VIDEO("video");

    /**
     * REST API value associated with the media type.
     */
    @JsonValue
    private final String value;
}
