package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.client.domain.model.enums.WpMediaStatus;
import io.github.evisentin.wordpress.client.domain.model.enums.WpMediaType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a WordPress media item as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a media object, including metadata such as identifiers, publication and
 * modification dates, rendered fields, publication status, media type, MIME type, file information, source URL,
 * alternative text, caption, description, and associated parent post.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with the API.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/media/">WordPress REST API - Media</a>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpMedia extends WpResource {

    /**
     * Current publication status of the media item.
     */
    private WpMediaStatus status;

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
     * Rendered description of the media item.
     */
    private WpRenderedField description;

    /**
     * Rendered caption of the media item.
     */
    private WpRenderedField caption;

    /**
     * Identifier of the parent post associated with the attachment.
     */
    @JsonProperty("post")
    private Long postId;
}
