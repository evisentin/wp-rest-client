package io.github.evisentin.wordpress.rest.client.samples.code.posts;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;

import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.PENDING;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.PUBLISH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to retrieve a paginated list of WordPress posts.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Filter posts using a {@link WpPostQuery}.</li>
 *     <li>Request a post of results using {@link WpPaginationQuery}.</li>
 *     <li>Access pagination metadata.</li>
 *     <li>Iterate over the returned {@link WpPost} instances.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class ListPosts {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // WpPostQuery provides many attributes for filtering posts. This example filters by post status only.
        // Refer to the class Javadoc for a complete description of all supported query parameters.
        final WpPostQuery query = WpPostQuery.builder()
                                             .withStatus(DRAFT)
                                             .withStatus(PUBLISH)
                                             .withStatus(PENDING)
                                             .build();

        final WpPaginationQuery pagingQuery = new WpPaginationQuery(1, 10);

        final WpPagedResponse<WpPost> pagedResponse =
                restClient.posts()
                          .list(pagingQuery, query);

        // Print pagination information.
        System.out.println("Page number   : " + pagedResponse.pageNumber());
        System.out.println("Items per page: " + pagedResponse.itemsPerPage());
        System.out.println("Total items   : " + pagedResponse.totalItems());
        System.out.println("Has next page : " + pagedResponse.hasNextPage());
        System.out.println("Is empty      : " + pagedResponse.isEmpty());

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
