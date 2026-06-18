package io.github.evisentin.wordpress.rest.client.domain.model.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.evisentin.wordpress.rest.client.domain.model.WpComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpCommentDeletionResponse {
    private boolean deleted;
    private WpComment previous;
}
