package io.github.evisentin.wordpress.rest.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a WordPress page as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a page object, including metadata such as identifiers, publication and
 * modification dates, rendered content fields, publication status, password protection.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with the API.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/pages/">WordPress REST API - Posts</a>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpPage extends WpResource {

    /**
     * Current publication status of the page.
     */
    private WpPageStatus status;

    /**
     * Password required to access the page, if protected.
     */
    private String password;

    /**
     * Rendered main content of the page.
     */
    private WpRenderedField content;

    @JsonProperty("parent")
    private Long parentId;

    @JsonProperty("menu_order")
    private Long menuOrder;
}
