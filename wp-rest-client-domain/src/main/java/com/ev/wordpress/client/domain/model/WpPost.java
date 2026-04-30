package com.ev.wordpress.client.domain.model;

import com.ev.wordpress.client.domain.model.enums.WpOpenClosed;
import com.ev.wordpress.client.domain.model.enums.WpPostFormat;
import com.ev.wordpress.client.domain.model.enums.WpPostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a WordPress post as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a post object, including metadata
 * such as identifiers, publication and modification dates, rendered content fields, status, taxonomy associations, and
 * additional configuration flags.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure
 * forward compatibility with the API.</p>
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpPost {

    /**
     * Unique identifier of the post.
     */
    private Long id;

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
     * Globally unique identifier for the post.
     */
    private WpRenderedField guid;

    /**
     * Direct URL to the post.
     */
    private String link;

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
     * URL-friendly slug of the post.
     */
    private String slug;

    /**
     * Automatically generated slug based on the title.
     */
    @JsonProperty("generated_slug")
    private String generatedSlug;

    /**
     * Current publication status of the post.
     */
    private WpPostStatus status;

    /**
     * Type of the post, such as {@code post}, {@code page}, or a custom post type.
     */
    private String type;

    /**
     * Password required to access the post, if protected.
     */
    private String password;

    /**
     * Permalink template used to generate the post URL.
     */
    @JsonProperty("permalink_template")
    private String permalinkTemplate;

    /**
     * Rendered title of the post.
     */
    private WpRenderedField title;

    /**
     * Rendered main content of the post.
     */
    private WpRenderedField content;

    /**
     * Rendered excerpt of the post.
     */
    private WpRenderedField excerpt;

    /**
     * Identifier of the post author.
     */
    @JsonProperty("author")
    private Long authorId;

    /**
     * Identifier of the featured media item.
     */
    @JsonProperty("featured_media")
    private Long featuredMediaId;

    /**
     * Comment status indicating whether comments are open or closed.
     */
    @JsonProperty("comment_status")
    private WpOpenClosed commentStatus;

    /**
     * Ping status indicating whether pings are open or closed.
     */
    @JsonProperty("ping_status")
    private WpOpenClosed pingStatus;

    /**
     * Post format.
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
