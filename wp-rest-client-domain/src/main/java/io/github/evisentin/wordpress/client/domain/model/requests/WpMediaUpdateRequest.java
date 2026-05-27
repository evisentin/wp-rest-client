package io.github.evisentin.wordpress.client.domain.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.client.domain.model.enums.WpMediaStatus;
import io.github.evisentin.wordpress.client.domain.model.enums.WpOpenClosed;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WpMediaUpdateRequest {
    /**
     * The date the media item was published, in the site's local timezone.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    /**
     * The date the media item was published, as GMT (UTC).
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("date_gmt")
    private LocalDateTime dateGMT;

    /**
     * An alphanumeric identifier for the media item unique to its type.
     */
    private String slug;

    /**
     * A named status for the media item.
     */
    private WpMediaStatus status;

    /**
     * The title for the media item.
     */
    private String title;

    /**
     * The ID of the user who created the media item.
     */
    @JsonProperty("author")
    private Long authorId;

    /**
     * The ID of the associated post for the media item.
     */
    @JsonProperty("post")
    private Long postId;

    /**
     * Whether comments are open or closed for the media item.
     */
    @JsonProperty("comment_status")
    private WpOpenClosed commentStatus;

    /**
     * Whether pingbacks or trackbacks are enabled.
     */
    @JsonProperty("ping_status")
    private WpOpenClosed pingStatus;

    /**
     * The theme file to use when displaying the media item.
     */
    private String template;

    /**
     * Alternative text to display when the media item cannot be rendered.
     */
    @JsonProperty("alt_text")
    private String altText;

    /**
     * The caption for the media item.
     */
    private String caption;

    /**
     * The description for the media item.
     */
    private String description;
}
