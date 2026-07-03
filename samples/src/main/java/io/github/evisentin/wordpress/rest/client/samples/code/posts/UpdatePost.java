package io.github.evisentin.wordpress.rest.client.samples.code.posts;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to update an existing WordPress post.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Build a {@link WpPostCreateUpdateRequest} containing only the fields to be updated.</li>
 *     <li>Update an existing post using its identifier.</li>
 *     <li>Access the returned {@link WpPost} instance.</li>
 * </ul>
 * <p>
 * The request performs a partial update: only the fields specified in the {@link WpPostCreateUpdateRequest} are
 * modified, while all other post attributes remain unchanged. In this example, the post title is updated and its
 * status is changed from {@code DRAFT} to {@code PUBLISH}.
 * <p>
 * Depending on the requested post and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if the current user is not allowed to update the post.</li>
 *     <li>{@code WpNotFoundException} if the post does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class UpdatePost {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // Given post id=100L exists and has DRAFT status;

        // All the fields not present in the updateRequest shall remain unchanged in the post after the update.
        final WpPostCreateUpdateRequest updateRequest =
                WpPostCreateUpdateRequest.builder()
                                         .withTitle("My new title")
                                         .withStatus(WpPostStatus.PUBLISH) // it was DRAFT before
                                         .build();

        final WpPost updatedPost = restClient.posts().update(100L, updateRequest);

        // PLEASE NOTE: you might get
        // - WpForbiddenException
        // - WpNotFoundException
        // - WpUnauthorizedException
    }
}
