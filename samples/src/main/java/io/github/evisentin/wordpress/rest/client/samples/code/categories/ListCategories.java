package io.github.evisentin.wordpress.rest.client.samples.code.categories;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpCategoryQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to retrieve a paginated list of WordPress categories.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Filter categories using a {@link WpCategoryQuery}.</li>
 *     <li>Request a page of results using {@link WpPaginationQuery}.</li>
 *     <li>Access pagination metadata.</li>
 *     <li>Iterate over the returned {@link WpCategory} instances.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class ListCategories {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // WpCategoryQuery provides many attributes for filtering categories. This example filters by post id only.
        // Refer to the class Javadoc for a complete description of all supported query parameters.
        final WpCategoryQuery query = WpCategoryQuery.builder()
                                                     .withPostId(100L)
                                                     .build();

        final WpPaginationQuery pagingQuery = new WpPaginationQuery(1, 10);

        final WpPagedResponse<WpCategory> pagedResponse =
                restClient.categories()
                          .list(pagingQuery, query);

        // Print pagination information.
        System.out.println("Page number   : " + pagedResponse.pageNumber());
        System.out.println("Items per page: " + pagedResponse.itemsPerPage());
        System.out.println("Total items   : " + pagedResponse.totalItems());
        System.out.println("Has next page : " + pagedResponse.hasNextPage());
        System.out.println("Is empty      : " + pagedResponse.isEmpty());

        // Print returned categories.
        pagedResponse.items()
                     .forEach(category ->
                             System.out.printf(
                                     "[id=%d] [name='%s'] [slug='%s'] [description='%s'] %n",
                                     category.getId(),
                                     category.getName(),
                                     category.getSlug(),
                                     category.getDescription()
                             )
                     );
    }
}
