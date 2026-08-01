package io.github.evisentin.wordpress.rest.client.samples.code.tags;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpTag;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpTagCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to create a new WordPress tag.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Build a {@link WpTagCreateUpdateRequest} using the builder API.</li>
 *     <li>Set common tag attributes such as the name, slug, description, and parent tag.</li>
 *     <li>Create the tag using the WordPress REST API.</li>
 *     <li>Access the returned {@link WpTag} instance.</li>
 * </ul>
 * <p>
 * The request intentionally sets a number of optional fields to demonstrate the available customization options. Most
 * of these attributes are optional and, if omitted, WordPress automatically supplies sensible defaults where
 * applicable (for example, the slug).
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class CreateTag {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        WpTagCreateUpdateRequest createRequest =
                WpTagCreateUpdateRequest.builder()
                                        .withName("My Tag")
                                        .withSlug("my-tag") // if not provided, it is computed by WordPress
                                        .withDescription("The description of my tag")
                                        .build();

        final WpTag wpTag = restClient.tags().create(createRequest);

        System.out.println("Tag created with id=" + wpTag.getId());
    }
}
