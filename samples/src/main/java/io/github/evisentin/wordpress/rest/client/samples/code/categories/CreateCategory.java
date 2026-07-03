package io.github.evisentin.wordpress.rest.client.samples.code.categories;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to create a new WordPress category.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Build a {@link WpCategoryCreateUpdateRequest} using the builder API.</li>
 *     <li>Set common category attributes such as the name, slug, description, and parent category.</li>
 *     <li>Create the category using the WordPress REST API.</li>
 *     <li>Access the returned {@link WpCategory} instance.</li>
 * </ul>
 * <p>
 * The request intentionally sets a number of optional fields to demonstrate the available customization options. Most
 * of these attributes are optional and, if omitted, WordPress automatically supplies sensible defaults where
 * applicable (for example, the slug).
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class CreateCategory {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        WpCategoryCreateUpdateRequest createRequest =
                WpCategoryCreateUpdateRequest.builder()
                                             .withName("My Category")
                                             .withSlug("my-category") // if not provided, it is computed by WordPress
                                             .withDescription("The description of my category")
                                             //.withParentId(1000L) // in case you want to add a parent.
                                             .build();

        final WpCategory wpCategory = restClient.categories().create(createRequest);

        System.out.println("Category created with id=" + wpCategory.getId());
    }
}
