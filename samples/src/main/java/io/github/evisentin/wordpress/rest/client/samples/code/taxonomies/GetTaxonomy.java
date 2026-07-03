package io.github.evisentin.wordpress.rest.client.samples.code.taxonomies;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpTaxonomyInfo;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to retrieve information about a single WordPress taxonomy.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Retrieve a taxonomy by its unique name.</li>
 *     <li>Access the returned {@link WpTaxonomyInfo} instance.</li>
 * </ul>
 * <p>
 * The returned {@link WpTaxonomyInfo} contains metadata describing the taxonomy, including its name, description,
 * slug, supported object types, and whether it is hierarchical.
 * <p>
 * Depending on the requested taxonomy and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if access to the taxonomy is denied.</li>
 *     <li>{@code WpNotFoundException} if the taxonomy does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class GetTaxonomy {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        final WpTaxonomyInfo taxonomyInfo = restClient.taxonomies().get("post_tag");
        //taxonomyInfo.getName();
        //taxonomyInfo.getDescription();
        //taxonomyInfo.getSlug();
        //taxonomyInfo.getTypes();
        //taxonomyInfo.isHierarchical();

        // PLEASE NOTE: you might get
        // - WpForbiddenException
        // - WpNotFoundException
        // - WpUnauthorizedException
    }
}
