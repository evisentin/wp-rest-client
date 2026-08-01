package io.github.evisentin.wordpress.rest.client.samples.code.tags;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpTagDeletionResponse;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to delete a WordPress tag.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Permanently delete a tag.</li>
 *     <li>Inspect the returned {@link WpTagDeletionResponse}.</li>
 *     <li>Access information about the deleted tag through the response summary.</li>
 * </ul>
 * <p>
 * Tags are permanently deleted by the WordPress REST API. The {@code delete()} operation returns a
 * {@link WpTagDeletionResponse} containing the deletion status together with a summary of the deleted tag.
 * <p>
 * Depending on the requested tag and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if the current user is not allowed to delete the tag.</li>
 *     <li>{@code WpNotFoundException} if the tag does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class DeleteTag {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // Given tag id=100L exists;

        final WpTagDeletionResponse deletionResponse = restClient.tags().delete(100L);

        final boolean deleted = deletionResponse.isDeleted(); // true/false
        final WpTagDeletionResponse.Summary previous = deletionResponse.getPrevious();
        // previous.getId();
        // previous.getCount();
        // previous.getDescription();
        // previous.getName();
        // previous.getSlug();
        // previous.getTaxonomy();
        // previous.getLink();

        // PLEASE NOTE: you might get
        // - WpForbiddenException
        // - WpNotFoundException
        // - WpUnauthorizedException
    }
}
