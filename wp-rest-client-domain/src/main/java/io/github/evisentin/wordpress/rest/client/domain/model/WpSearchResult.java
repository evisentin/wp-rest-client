package io.github.evisentin.wordpress.rest.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpSearchResult {

    /**
     * Unique identifier.
     */
    private Long id;

    private String title;
    private String url;
    private String type;

    @JsonProperty("subtype")
    private String subType;
}
