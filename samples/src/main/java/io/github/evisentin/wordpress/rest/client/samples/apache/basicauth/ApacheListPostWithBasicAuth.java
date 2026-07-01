package io.github.evisentin.wordpress.rest.client.samples.apache.basicauth;

import io.github.evisentin.wordpress.rest.client.adapter.apache.ApacheWpRestClientBuilder;
import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.rest.client.samples.apache.SampleHttpRequestInterceptor;
import io.github.evisentin.wordpress.rest.client.samples.apache.SampleHttpResponseInterceptor;

import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.PENDING;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.PUBLISH;

/**
 * Demonstrates how to authenticate against the WordPress REST API using HTTP Basic Authentication and retrieve a
 * paginated list of posts.
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
public class ApacheListPostWithBasicAuth {

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
     * WordPress username used for authentication.
     */
    private static final String USER_NAME = "admin";

    /**
     * Password used for authentication.
     */
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {

        final WpRestClient restClient =
                ApacheWpRestClientBuilder.basicAuthentication(BASE_URL, USER_NAME, PASSWORD)
                                         .withInterceptor(new SampleHttpRequestInterceptor())
                                         .withInterceptor(new SampleHttpResponseInterceptor())
                                         .build();

        final WpPostQuery postQuery = WpPostQuery.builder()
                                                 .withStatus(DRAFT)
                                                 .withStatus(PUBLISH)
                                                 .withStatus(PENDING)
                                                 .build();

        final WpPaginationQuery pagingQuery = new WpPaginationQuery(1, 10);

        final WpPagedResponse<WpPost> pagedResponse = restClient.posts().list(pagingQuery, postQuery);

        // Print pagination information.
        System.out.println("Page number     : " + pagedResponse.pageNumber());
        System.out.println("Items per page  : " + pagedResponse.itemsPerPage());
        System.out.println("Total items     : " + pagedResponse.totalItems());
        System.out.println("Has next page   : " + pagedResponse.hasNextPage());
        System.out.println("Is empty        : " + pagedResponse.isEmpty());

        // Print returned posts.
        pagedResponse.items()
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
