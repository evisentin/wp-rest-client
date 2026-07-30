package io.github.evisentin.wordpress.rest.client.samples.code.tags;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpTag;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to retrieve a single WordPress tag.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Retrieve a tag by its identifier.</li>
 *     <li>Specify the desired {@link WpContext} for the response.</li>
 *     <li>Access the returned {@link WpTag} instance.</li>
 * </ul>
 * <p>
 * The {@link WpContext} controls which fields are included in the response. Public tags are typically retrieved
 * using {@code VIEW}, while authenticated users can request {@code EDIT} to obtain additional information.
 * <p>
 * Depending on the requested tag and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if access to the tag is denied.</li>
 *     <li>{@code WpNotFoundException} if the tag does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class GetTag {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        final WpContext wpContext = WpContext.EDIT; /* or WpContext.VIEW, or WpContext.EMBED */

        // Given tag id=100L exists;
        final WpTag tag = restClient.tags().get(100L, wpContext);

        // PLEASE NOTE: you might get
        // - WpForbiddenException
        // - WpNotFoundException
        // - WpUnauthorizedException
    }
}
