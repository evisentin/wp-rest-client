package io.github.evisentin.wordpress.rest.client.samples.okhttp.basicauth;

import io.github.evisentin.wordpress.rest.client.adapter.okhttp.OkHttpWpRestClientBuilder;
import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.rest.client.samples.okhttp.SampleHttpRequestInterceptor;
import io.github.evisentin.wordpress.rest.client.samples.okhttp.SampleHttpResponseInterceptor;

import java.util.Set;

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
public class OkHttpListPostWithBasicAuth {

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
                OkHttpWpRestClientBuilder.basicAuthentication(BASE_URL, USER_NAME, PASSWORD)
                                         .withInterceptor(new SampleHttpRequestInterceptor())
                                         .withInterceptor(new SampleHttpResponseInterceptor())
                                         .build();

        final WpPostQuery postQuery = WpPostQuery.builder()
                                                 .withStatuses(Set.of(DRAFT, PUBLISH, PENDING))
                                                 .build();

        final WpPaginationQuery pagingQuery = new WpPaginationQuery(1, 10);

        final WpPagedResponse<WpPost> pagedResponse = restClient.posts().list(pagingQuery, postQuery);

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
