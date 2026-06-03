package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.client.domain.model.enums.WpOpenClosed;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Base class for WordPress content resources exposed through the WordPress REST API.
 *
 * <p>This model contains the metadata and configuration fields that are shared by multiple WordPress resource types,
 * such as posts, pages, media attachments, and custom post types. It provides common properties including identifiers,
 * publication and modification dates, permalink information, rendered fields, authorship metadata, and discussion
 * settings.</p>
 *
 * <p>Concrete resource implementations extend this class to add resource-specific attributes while inheriting the
 * common structure defined by the WordPress REST API.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with future WordPress
 * API versions.</p>
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class WpResource {

    /**
     * Unique identifier.
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
     * Globally unique identifier.
     */
    private WpRenderedField guid;

    /**
     * Direct URL to the resource.
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
     * URL-friendly slug.
     */
    private String slug;

    /**
     * Automatically generated slug based on the title.
     */
    @JsonProperty("generated_slug")
    private String generatedSlug;

    /**
     * Resource type.
     */
    private String type;

    /**
     * Permalink template used to generate the resource URL.
     */
    @JsonProperty("permalink_template")
    private String permalinkTemplate;

    /**
     * Rendered title.
     */
    private WpRenderedField title;

    /**
     * Rendered excerpt.
     */
    private WpRenderedField excerpt;

    /**
     * Identifier of the author.
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
     * Theme template assigned to the resource.
     */
    private String template;
}
