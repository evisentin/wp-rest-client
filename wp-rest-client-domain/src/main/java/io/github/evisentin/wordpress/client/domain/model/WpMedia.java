package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.client.domain.model.enums.WpMediaStatus;
import io.github.evisentin.wordpress.client.domain.model.enums.WpMediaType;
import io.github.evisentin.wordpress.client.domain.model.enums.WpOpenClosed;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a WordPress media item as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a media object, including metadata
 * such as identifiers, publication and modification dates, rendered fields, media metadata, attachment information,
 * publication status, and additional configuration flags.</p>
 *
 * <p>Media items in WordPress are represented as {@code attachment} posts and
 * expose information such as MIME type, file size, captions, descriptions, and source URLs.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure
 * forward compatibility with the API.</p>
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpMedia {

    /**
     * Unique identifier of the media item.
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
     * Globally unique identifier for the media item.
     */
    private WpRenderedField guid;

    /**
     * Direct URL to the media item.
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
     * URL-friendly slug of the media item.
     */
    private String slug;

    /**
     * Automatically generated slug based on the title.
     */
    @JsonProperty("generated_slug")
    private String generatedSlug;

    /**
     * Current publication status of the media item.
     */
    private WpMediaStatus status;

    /**
     * Type of the media item, typically {@code attachment}.
     */
    private String type;

    /**
     * High-level media type, such as {@code image}, {@code video}, {@code audio}, or {@code file}.
     */
    @JsonProperty("media_type")
    private WpMediaType mediaType;

    /**
     * MIME type of the media file.
     */
    @JsonProperty("mime_type")
    private String mimeType;

    /**
     * Original filename of the uploaded media file.
     */
    @JsonProperty("filename")
    private String fileName;

    /**
     * Size of the media file in bytes.
     */
    @JsonProperty("filesize")
    private Long fileSize;

    /**
     * Direct URL of the uploaded media file.
     */
    @JsonProperty("source_url")
    private String sourceUrl;

    /**
     * Alternative text associated with the media item.
     */
    @JsonProperty("alt_text")
    private String altText;

    /**
     * Permalink template used to generate the media item URL.
     */
    @JsonProperty("permalink_template")
    private String permalinkTemplate;

    /**
     * Rendered title of the media item.
     */
    private WpRenderedField title;

    /**
     * Rendered description of the media item.
     */
    private WpRenderedField description;

    /**
     * Rendered caption of the media item.
     */
    private WpRenderedField caption;

    /**
     * Rendered excerpt of the media item.
     */
    private WpRenderedField excerpt;

    /**
     * Identifier of the media item author.
     */
    @JsonProperty("author")
    private Long authorId;

    /**
     * Identifier of the parent post associated with the attachment.
     */
    @JsonProperty("post")
    private Long postId;

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
     * Theme template assigned to the media item.
     */
    private String template;
}
