package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.client.domain.model.enums.WpCommentStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents a WordPress comment as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a comment object, including metadata such as its identifier, associated
 * post, parent comment, author information, publication dates, rendered content, moderation status, type, and avatar
 * URLs.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with the API.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/comments/">WordPress REST API - Comments</a>
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpComment {

    /**
     * Unique identifier for the comment.
     */
    private Long id;

    /**
     * Identifier of the post to which this comment belongs.
     */
    private Long post;

    /**
     * Identifier of the parent comment. A value of {@code 0} or {@code null} indicates a top-level comment.
     */
    private Long parent;

    /**
     * Identifier of the comment author. May be {@code null} for anonymous commenters.
     */
    @JsonProperty("author")
    private Long authorId;

    /**
     * Display name of the comment author.
     */
    @JsonProperty("author_name")
    private String authorName;

    /**
     * URL provided by the comment author.
     */
    @JsonProperty("author_url")
    private String authorUrl;

    /**
     * Publication date in the site's local timezone.
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
     * Rendered comment content.
     */
    private WpRenderedField content;

    /**
     * Permanent URL of the comment.
     */
    private String link;

    /**
     * Current moderation status of the comment.
     */
    private WpCommentStatus status;

    /**
     * Comment type. Typically {@code "comment"}, but may also represent pingbacks or trackbacks.
     */
    private String type;

    /**
     * Map of author avatar URLs keyed by avatar size in pixels. Example keys are {@code "24"}, {@code "48"}, and
     * {@code "96"}.
     */
    @JsonProperty("author_avatar_urls")
    private Map<String, String> authorAvatarUrls;
}
