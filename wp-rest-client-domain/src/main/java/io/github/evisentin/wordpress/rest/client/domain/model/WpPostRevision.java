package io.github.evisentin.wordpress.rest.client.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a revision of a WordPress post as returned by the WordPress REST API.
 *
 * <p>A post revision is a snapshot of a post's content, title, and excerpt at a specific point in time. Revisions are
 * automatically created by WordPress when a post is updated and can be used to inspect or restore previous versions of
 * the post.</p>
 *
 * <p>This model maps the JSON structure of a revision object, including metadata such as identifiers, author
 * information, creation and modification dates, rendered content fields, and the parent post reference.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with the API.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/post-revisions/">WordPress REST API - Post
 * Revisions</a>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpPostRevision {

    /**
     * Unique identifier.
     */
    private Long id;

    @JsonProperty("parent")
    private Long parentId;

    /**
     * Identifier of the revision author.
     */
    @JsonProperty("author")
    private Long authorId;

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
     * Globally unique identifier.
     */
    private WpRenderedField guid;

    /**
     * Last modification date in local time.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modified;

    /**
     * Last modification date in GMT (UTC).
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("modified_gmt")
    private LocalDateTime modifiedGMT;

    /**
     * URL-friendly slug.
     */
    private String slug;

    /**
     * Rendered title.
     */
    private WpRenderedField title;

    /**
     * Rendered excerpt.
     */
    private WpRenderedField excerpt;

    /**
     * Rendered main content of the post.
     */
    private WpRenderedField content;
}
