package io.github.evisentin.wordpress.rest.client.samples.code.pages;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPage;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPageQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus.DRAFT;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus.PENDING;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus.PUBLISH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to retrieve a paginated list of WordPress pages.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Filter pages using a {@link WpPageQuery}.</li>
 *     <li>Request a page of results using {@link WpPaginationQuery}.</li>
 *     <li>Access pagination metadata.</li>
 *     <li>Iterate over the returned {@link WpPage} instances.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class ListPages {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // WpPageQuery provides many attributes for filtering pages. This example filters by page status only.
        // Refer to the class Javadoc for a complete description of all supported query parameters.
        final WpPageQuery query = WpPageQuery.builder()
                                             .withStatus(DRAFT)
                                             .withStatus(PUBLISH)
                                             .withStatus(PENDING)
                                             .build();
        final WpPaginationQuery pagingQuery = new WpPaginationQuery(1, 10);

        final WpPagedResponse<WpPage> pagedResponse =
                restClient.pages()
                          .list(pagingQuery, query);

        // Print pagination information.
        System.out.println("Page number   : " + pagedResponse.pageNumber());
        System.out.println("Items per page: " + pagedResponse.itemsPerPage());
        System.out.println("Total items   : " + pagedResponse.totalItems());
        System.out.println("Has next page : " + pagedResponse.hasNextPage());
        System.out.println("Is empty      : " + pagedResponse.isEmpty());

        // Print returned pages.
        pagedResponse.items()
                     .forEach(page ->
                             System.out.printf(
                                     "[id=%d] [title='%s'] [slug='%s'] [status='%s'] %n",
                                     page.getId(),
                                     page.getTitle().getRendered(),
                                     page.getSlug(),
                                     page.getStatus()
                             )
                     );
    }
}
