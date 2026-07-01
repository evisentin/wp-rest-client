package io.github.evisentin.wordpress.rest.client.domain.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpOpenClosed;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Request body used to create or update a WordPress page.
 *
 * <p>This model maps the JSON arguments accepted by the WordPress Pages REST API endpoint, including publication
 * dates, slug, status, password, title, content, excerpt, author, featured media, discussion settings, page format,
 * sticky flag, template, categories, and tags.</p>
 *
 * <p>Null values are omitted during serialization.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/posts/#arguments-2">WordPress REST API -
 * Create a Page Arguments</a>
 */
@Getter
@Setter
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WpPageCreateUpdateRequest {

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
     * URL-friendly slug of the page.
     */
    private String slug;

    /**
     * Current publication status of the page (e.g., publish, draft, pending, private).
     */
    private WpPageStatus status;

    /**
     * Password required to access the page, if protected.
     */
    private String password;

    /**
     * Rendered title of the page.
     */
    private String title;

    /**
     * Rendered main content of the page (HTML).
     */
    private String content;

    /**
     * Rendered excerpt (summary) of the page.
     */
    private String excerpt;

    /**
     * Identifier of the page author.
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
     * Theme template assigned to the page.
     */
    private String template;
}
