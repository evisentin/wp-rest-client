package com.ev.wordpress.client.domain.model.responses;

import com.ev.wordpress.client.domain.model.WpRenderedField;
import com.ev.wordpress.client.domain.model.enums.WpPostStatus;
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

        private String link;
        private WpRenderedField title;
        private WpRenderedField content;
        private WpRenderedField excerpt;
        private String slug;
        private WpPostStatus status;
    }
}
