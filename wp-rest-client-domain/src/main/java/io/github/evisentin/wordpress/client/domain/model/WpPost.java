package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.evisentin.wordpress.client.domain.model.enums.WpPostFormat;
import io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Represents a WordPress post as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a post object, including metadata such as identifiers, publication and
 * modification dates, rendered content fields, publication status, post format, taxonomy associations, password
 * protection, and sticky flag.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with the API.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/posts/">WordPress REST API - Posts</a>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpPost extends WpResource {

    /**
     * Current publication status of the post.
     */
    private WpPostStatus status;

    /**
     * Password required to access the post, if protected.
     */
    private String password;

    /**
     * Rendered main content of the post.
     */
    private WpRenderedField content;

    /**
     * Post format.
     */
    private WpPostFormat format;

    /**
     * Indicates whether the post is marked as sticky.
     */
    private boolean sticky;

    /**
     * Set of category IDs associated with the post.
     */
    private Set<Long> categories;

    /**
     * Set of tag IDs associated with the post.
     */
    private Set<Long> tags;
}
