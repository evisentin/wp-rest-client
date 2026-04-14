package com.ev.wordpress.client.domain.dto.responses;

import com.ev.wordpress.client.domain.dto.enums.WpPostStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpPostDeletionResponse {
    private boolean deleted;
    private Summary previous;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {
        private long id;

        private String description;
        private String link;
        private String name;
        private String slug;
        private WpPostStatus status;
    }
}
