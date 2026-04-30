package com.ev.wordpress.client.domain.model.responses;

import com.ev.wordpress.client.domain.model.enums.WpTaxonomy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpTermDeletionResponse {
    private boolean deleted;
    private Summary previous;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {
        private long id;
        private long count;
        private String description;
        private String link;
        private String name;
        private String slug;
        private WpTaxonomy taxonomy;
    }
}
