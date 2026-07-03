package io.github.evisentin.wordpress.rest.client.samples.code.pages;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPage;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPageDeletionResponse;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to delete a WordPress page.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Move a page to the WordPress trash.</li>
 *     <li>Permanently delete a page.</li>
 *     <li>Inspect the returned {@link WpPageDeletionResponse}.</li>
 *     <li>Access information about the deleted page through the response summary.</li>
 * </ul>
 * <p>
 * The sample demonstrates the two deletion modes supported by the WordPress REST API. Calling {@code trash()} moves
 * the page to the trash, allowing it to be restored later. Calling {@code delete()} permanently removes the page
 * and returns a {@link WpPageDeletionResponse} containing the deletion status together with a summary of the deleted page.
 * <p>
 * Depending on the requested page and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if the current user is not allowed to delete the page.</li>
 *     <li>{@code WpNotFoundException} if the page does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class DeletePage {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // Given page id=100L exists;

        // here the page is marked as ( wpPage.getStatus() == WpPageStatus.TRASH), but still exists, it is possible to recover it.
        final WpPage wpPage = restClient.pages().trash(100L);

        // here the page is permanently deleted.
        final WpPageDeletionResponse deletionResponse = restClient.pages().delete(100L);

        final boolean deleted = deletionResponse.isDeleted();// true/false
        final WpPageDeletionResponse.Summary previous = deletionResponse.getPrevious();
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
