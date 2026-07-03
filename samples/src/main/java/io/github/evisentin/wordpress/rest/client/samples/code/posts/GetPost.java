package io.github.evisentin.wordpress.rest.client.samples.code.posts;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to retrieve a single WordPress post.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Retrieve a post by its identifier.</li>
 *     <li>Specify the desired {@link WpContext} for the response.</li>
 *     <li>Retrieve a password-protected post by supplying its password.</li>
 *     <li>Access the returned {@link WpPost} instance.</li>
 * </ul>
 * <p>
 * The example shows both retrieving a standard post and accessing a password-protected post.
 * The {@link WpContext} controls which fields are included in the response. Public posts are typically retrieved using
 * {@code VIEW}, while authenticated users can request {@code EDIT} to obtain additional information.
 * <p>
 * Depending on the requested post and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if access to the post is denied.</li>
 *     <li>{@code WpNotFoundException} if the post does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class GetPost {
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

        // Given post id=100L exists;
        final WpPost wpPost = restClient.posts().get(100L, wpContext);

        // Given post id=200L exists and has been created with a password;
        final WpPost wpPostWithPassword = restClient.posts().get(100L, wpContext, "my-password");

        // PLEASE NOTE: you might get
        // - WpForbiddenException
        // - WpNotFoundException
        // - WpUnauthorizedException
    }
}
