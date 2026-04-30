package com.ev.wordpress.client.domain.model.requests;

import com.ev.wordpress.client.domain.model.enums.WpOpenClosed;
import com.ev.wordpress.client.domain.model.enums.WpPostFormat;
import com.ev.wordpress.client.domain.model.enums.WpPostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WpPostCreateUpdateRequest {

    /**
     * Publication date in local time.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    /**
     * Publication date in GMT (UTC).
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("date_gmt")
    private LocalDateTime dateGMT;

    /**
     * URL-friendly slug of the post.
     */
    private String slug;

    /**
     * Current publication status of the post (e.g., publish, draft, pending, private).
     */
    private WpPostStatus status;

    /**
     * Password required to access the post, if protected.
     */
    private String password;

    /**
     * Rendered title of the post.
     */
    private String title;

    /**
     * Rendered main content of the post (HTML).
     */
    private String content;

    /**
     * Rendered excerpt (summary) of the post.
     */
    private String excerpt;

    /**
     * Identifier of the post author.
     */
    @JsonProperty("author")
    private Long authorId;

    /**
     * Identifier of the featured media (e.g., image).
     */
    @JsonProperty("featured_media")
    private Long featuredMediaId;

    /**
     * Comment status indicating whether comments are open or closed.
     */
    @JsonProperty("comment_status")
    private WpOpenClosed commentStatus;

    /**
     * Ping (trackback/pingback) status indicating whether pings are open or closed.
     */
    @JsonProperty("ping_status")
    private WpOpenClosed pingStatus;

    /**
     * Post format (e.g., standard, image, video, gallery).
     */
    private WpPostFormat format;

    /**
     * Indicates whether the post is marked as sticky.
     */
    private boolean sticky;

    /**
     * Theme template assigned to the post.
     */
    private String template;

    /**
     * Set of category IDs associated with the post.
     */
    private Set<Long> categories;

    /**
     * Set of tag IDs associated with the post.
     */
    private Set<Long> tags;
}
