package io.github.evisentin.wordpress.rest.client.samples.code.categories;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder;

import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.AuthenticationType.BASIC_AUTH;
import static io.github.evisentin.wordpress.rest.client.samples.code.SampleClientBuilder.Implementation.APACHE;

/**
 * Demonstrates how to update an existing WordPress category.
 * <p>
 * This sample illustrates how to:
 * <ul>
 *     <li>Create a {@link WpRestClient}.</li>
 *     <li>Build a {@link WpCategoryCreateUpdateRequest} containing only the fields to be updated.</li>
 *     <li>Update an existing category using its identifier.</li>
 *     <li>Access the returned {@link WpCategory} instance.</li>
 * </ul>
 * <p>
 * The request performs a partial update: only the fields specified in the
 * {@link WpCategoryCreateUpdateRequest} are modified, while all other category attributes remain unchanged. In this
 * example, the category description is updated.
 * <p>
 * Depending on the requested category and the current user's permissions, the client may throw exceptions such as:
 * <ul>
 *     <li>{@code WpForbiddenException} if the current user is not allowed to update the category.</li>
 *     <li>{@code WpNotFoundException} if the category does not exist.</li>
 *     <li>{@code WpUnauthorizedException} if authentication is required or invalid.</li>
 * </ul>
 * <p>
 * Before running this sample, configure the connection details in {@link SampleClientBuilder} so they match your
 * WordPress installation.
 */
public class UpdateCategory {
    public static void main(String[] args) {

        // Choose the desired HTTP client implementation and authentication mechanism.
        //
        // Supported combinations:
        //   buildClientFor(BASIC_AUTH, APACHE)
        //   buildClientFor(JWT,        APACHE)
        //   buildClientFor(BASIC_AUTH, OK_HTTP)
        //   buildClientFor(JWT,        OK_HTTP)
        final WpRestClient restClient = SampleClientBuilder.buildClientFor(BASIC_AUTH, APACHE);

        // Given category id=100L exists

        // All the fields not present in the updateRequest shall remain unchanged in the category after the update.
        final WpCategoryCreateUpdateRequest updateRequest =
                WpCategoryCreateUpdateRequest.builder()
                                             .withDescription("My new description")
                                             .build();

        final WpCategory updatedCategory = restClient.categories().update(100L, updateRequest);

        // PLEASE NOTE: you might get
        // - WpForbiddenException
        // - WpNotFoundException
        // - WpUnauthorizedException
    }
}
