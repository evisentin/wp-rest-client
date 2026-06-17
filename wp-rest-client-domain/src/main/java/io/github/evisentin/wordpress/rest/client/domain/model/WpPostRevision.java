package io.github.evisentin.wordpress.rest.client.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
//@EqualsAndHashCode(callSuper = true)
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
