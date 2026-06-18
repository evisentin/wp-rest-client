package io.github.evisentin.wordpress.rest.client.domain.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpCommentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Request body used to create or update a WordPress comment.
 *
 * <p>This model maps the JSON arguments accepted by the WordPress Comments REST API endpoint, including author
 * information, content, publication dates, parent comment, associated post, and moderation status.</p>
 *
 * <p>Null values are omitted during serialization.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/comments/#create-a-comment">WordPress REST API -
 * Create a Comment</a>
 */
@Getter
@Setter
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WpCommentCreateUpdateRequest {

    /**
     * Identifier of the user object, if the comment author is a WordPress user.
     */
    @JsonProperty("author")
    private Long authorId;

    /**
     * Email address of the comment author.
     */
    @JsonProperty("author_email")
    private String authorEmail;

    /**
     * Display name of the comment author.
     */
    @JsonProperty("author_name")
    private String authorName;

    /**
     * IP address of the comment author.
     */
    @JsonProperty("author_ip")
    private String authorIp;

    /**
     * URL of the comment author.
     */
    @JsonProperty("author_url")
    private String authorUrl;

    /**
     * User agent of the comment author.
     */
    @JsonProperty("author_user_agent")
    private String authorUserAgent;

    /**
     * Content of the comment.
     */
    private String content;

    /**
     * Publication date of the comment in the site's local timezone.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    /**
     * Publication date of the comment in GMT (UTC).
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("date_gmt")
    private LocalDateTime dateGMT;

    /**
     * Identifier of the parent comment.
     */
    @JsonProperty("parent")
    private Long parentId;

    /**
     * Identifier of the associated post. (CANNOT BE NULL)
     */
    @JsonProperty("post")
    private long postId;

    /**
     * Moderation status of the comment.
     */
    private WpCommentStatus status;

    private String password;
}


