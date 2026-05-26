package io.github.evisentin.wordpress.client.domain.model.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.evisentin.wordpress.client.domain.model.WpMedia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpMediaDeletionResponse {
    private boolean deleted;
    private WpMedia previous;
}
