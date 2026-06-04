package io.github.evisentin.wordpress.client.samples.apache.jwt;

import io.github.evisentin.wordpress.client.adapter.apache.ApacheWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.client.samples.apache.SampleHttpRequestInterceptor;
import io.github.evisentin.wordpress.client.samples.apache.SampleHttpResponseInterceptor;

import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PENDING;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PUBLISH;

/**
 * Demonstrates how to authenticate against the WordPress REST API using JWT Authentication and retrieve a paginated
 * list of posts.
 *
 * <p>This example:
 * <ul>
 *     <li>Creates a REST client using Basic Authentication.</li>
 *     <li>Queries posts with specific statuses.</li>
 *     <li>Requests the first page of results.</li>
 *     <li>Prints pagination metadata and returned posts.</li>
 * </ul>
 * </p>
 */
public class ApacheListPostWithJwt {

    /**
     * Base URL of the target WordPress installation.
     *
     * <p>This must be the root URL of the WordPress site (for example {@code https://example.com}) and not the REST
     * API endpoint itself.
     *
     * <p>The client uses the WordPress discovery mechanism to automatically locate the REST API endpoint exposed by
     * the target installation.
     */
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Relative path of the JWT token endpoint.
     *
     * <p>This path is resolved relative to the WordPress REST API URL discovered by the client.</p>
     *
     * <p>For example:</p>
     * <ul>
     *     <li>Discovered REST API URL: {@code https://example.com/wp-json}</li>
     *     <li>JWT token endpoint: {@code /jwt-auth/v1/token}</li>
     *     <li>Resulting token URL: {@code https://example.com/wp-json/jwt-auth/v1/token}</li>
     * </ul>
     *
     * <p>The exact endpoint depends on the JWT authentication plugin installed on the target WordPress instance.</p>
     */
    private static final String JWT_TOKEN_ENDPOINT = "/api/v1/token";

    /**
     * WordPress username used for authentication.
     */
    private static final String USER_NAME = "admin";

    /**
     * Password used for authentication.
     */
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {

        final WpRestClient restClient =
                ApacheWpRestClientBuilder.jwtAuthentication(BASE_URL, USER_NAME, PASSWORD, JWT_TOKEN_ENDPOINT)
                                         .withInterceptor(new SampleHttpRequestInterceptor())
                                         .withInterceptor(new SampleHttpResponseInterceptor())
                                         .build();

        final WpPostQuery postQuery = WpPostQuery.builder()
                                                 .withStatuses(Set.of(DRAFT, PUBLISH, PENDING))
                                                 .build();

        final WpPagingQuery pagingQuery = WpPagingQuery.of(1, 10);

        final WpPagedResponse<WpPost> pagedResponse = restClient.listPosts(pagingQuery, postQuery);

        // Print pagination information.
        System.out.println("Page number     : " + pagedResponse.getPageNumber());
        System.out.println("Items per page  : " + pagedResponse.getItemsPerPage());
        System.out.println("Total items     : " + pagedResponse.getTotalItems());
        System.out.println("Has next page   : " + pagedResponse.hasNextPage());
        System.out.println("Is empty        : " + pagedResponse.isEmpty());

        // Print returned posts.
        pagedResponse.getItems()
                     .forEach(post ->
                             System.out.printf(
                                     "[id=%d] [title='%s'] [slug='%s'] [status='%s'] %n",
                                     post.getId(),
                                     post.getTitle().getRendered(),
                                     post.getSlug(),
                                     post.getStatus()
                             )
                     );
    }
}
