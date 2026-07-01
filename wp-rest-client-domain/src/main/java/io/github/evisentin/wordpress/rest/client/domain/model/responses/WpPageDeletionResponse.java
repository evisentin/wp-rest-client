package io.github.evisentin.wordpress.rest.client.domain.model.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.evisentin.wordpress.rest.client.domain.model.WpRenderedField;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpPageDeletionResponse {
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
        private WpPageStatus status;
    }
}
