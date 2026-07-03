package io.github.evisentin.wordpress.rest.client.samples.code.taxonomies;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpTaxonomyInfo;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to retrieve the available WordPress taxonomies.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Retrieve all taxonomies available on the WordPress installation.</li>
 *     <li>Access the returned {@link Map} keyed by taxonomy name.</li>
 *     <li>Inspect the {@link WpTaxonomyInfo} associated with each taxonomy.</li>
 * </ul>
 * <p>
 * The WordPress REST API returns the available taxonomies as a map whose keys are the taxonomy names (for example,
 * {@code category} and {@code post_tag}) and whose values contain the corresponding taxonomy metadata.
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class ListTaxonomies {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // Returns a map, the key is the taxonomy's unique name.
        final Map<String, WpTaxonomyInfo> taxonomiesByName = restClient.taxonomies().list();
    }
}
