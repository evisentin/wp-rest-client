package io.github.evisentin.wordpress.rest.client.samples.code.tags;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.WpTag;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpTagQuery;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to retrieve a paginated list of WordPress tags.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Filter tags using a {@link WpTagQuery}.</li>
 *     <li>Request a page of results using {@link WpPaginationQuery}.</li>
 *     <li>Access pagination metadata.</li>
 *     <li>Iterate over the returned {@link WpTag} instances.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class ListTags {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // WpTagQuery provides many attributes for filtering tags. This example filters by post id only.
        // Refer to the class Javadoc for a complete description of all supported query parameters.
        final WpTagQuery query = WpTagQuery.builder()
                                           .withPostId(100L)
                                           .build();

        final WpPaginationQuery pagingQuery = new WpPaginationQuery(1, 10);

        final WpPagedResponse<WpTag> pagedResponse =
                restClient.tags()
                          .list(pagingQuery, query);

        // Print pagination information.
        System.out.println("Page number   : " + pagedResponse.pageNumber());
        System.out.println("Items per page: " + pagedResponse.itemsPerPage());
        System.out.println("Total items   : " + pagedResponse.totalItems());
        System.out.println("Has next page : " + pagedResponse.hasNextPage());
        System.out.println("Is empty      : " + pagedResponse.isEmpty());

        // Print returned tags.
        pagedResponse.items()
                     .forEach(tag ->
                             System.out.printf(
                                     "[id=%d] [name='%s'] [slug='%s'] [description='%s'] %n",
                                     tag.getId(),
                                     tag.getName(),
                                     tag.getSlug(),
                                     tag.getDescription()
                             )
                     );
    }
}
