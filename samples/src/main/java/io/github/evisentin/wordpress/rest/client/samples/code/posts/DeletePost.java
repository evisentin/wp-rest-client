package io.github.evisentin.wordpress.rest.client.samples.code.posts;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPostDeletionResponse;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to delete a WordPress post.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Move a post to the WordPress trash.</li>
 *     <li>Permanently delete a post.</li>
 *     <li>Inspect the returned {@link WpPostDeletionResponse}.</li>
 *     <li>Access information about the deleted post through the response summary.</li>
 * </ul>
 * <p>
 * The sample demonstrates the two deletion modes supported by the WordPress REST API. Calling {@code trash()} moves
 * the post to the trash, allowing it to be restored later. Calling {@code delete()} permanently removes the post
 * and returns a {@link WpPostDeletionResponse} containing the deletion status together with a summary of the deleted post.
 * <p>
 * Depending on the requested post and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if the current user is not allowed to delete the post.</li>
 *     <li>{@code WpNotFoundException} if the post does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class DeletePost {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // Given post id=100L exists;

        // here the post is marked as ( wpPost.getStatus() == WpPostStatus.TRASH), but still exists, it is possible to recover it.
        final WpPost wpPost = restClient.posts().trash(100L);

        // here the post is permanently deleted.
        final WpPostDeletionResponse deletionResponse = restClient.posts().delete(100L);

        final boolean deleted = deletionResponse.isDeleted();// true/false
        final WpPostDeletionResponse.Summary previous = deletionResponse.getPrevious();
        // previous.getId();
        // previous.getTitle();
        // previous.getContent();
        // previous.getExcerpt();
        // previous.getSlug();
        // previous.getStatus();
        // previous.getLink();

        // PLEASE NOTE: you might get
        // - WpForbiddenException
        // - WpNotFoundException
        // - WpUnauthorizedException
    }
}
