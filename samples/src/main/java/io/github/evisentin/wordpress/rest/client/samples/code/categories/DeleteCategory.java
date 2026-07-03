package io.github.evisentin.wordpress.rest.client.samples.code.categories;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpCategoryDeletionResponse;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to delete a WordPress category.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Permanently delete a category.</li>
 *     <li>Inspect the returned {@link WpCategoryDeletionResponse}.</li>
 *     <li>Access information about the deleted category through the response summary.</li>
 * </ul>
 * <p>
 * Categories are permanently deleted by the WordPress REST API. The {@code delete()} operation returns a
 * {@link WpCategoryDeletionResponse} containing the deletion status together with a summary of the deleted category.
 * <p>
 * Depending on the requested category and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if the current user is not allowed to delete the category.</li>
 *     <li>{@code WpNotFoundException} if the category does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class DeleteCategory {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // Given category id=100L exists;

        final WpCategoryDeletionResponse deletionResponse = restClient.categories().delete(100L);

        final boolean deleted = deletionResponse.isDeleted();// true/false
        final WpCategoryDeletionResponse.Summary previous = deletionResponse.getPrevious();
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
