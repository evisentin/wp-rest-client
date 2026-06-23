package io.github.evisentin.wordpress.rest.client.contract.test;

import io.github.evisentin.wordpress.rest.client.domain.WpBaseRestClient;
import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.assertions.WordPressAssertions;
import io.github.evisentin.wordpress.rest.client.domain.model.*;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.*;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpCommentOrderFields;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPageOrderFields;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPostOrderFields;
import io.github.evisentin.wordpress.rest.client.domain.model.query.*;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.*;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.*;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.evisentin.wordpress.rest.client.contract.test.SlugUtils.toWordPressSlug;
import static io.github.evisentin.wordpress.rest.client.contract.test.WpAssertions.assertThrowsWpBadRequest;
import static io.github.evisentin.wordpress.rest.client.contract.test.WpAssertions.assertThrowsWpForbidden;
import static io.github.evisentin.wordpress.rest.client.contract.test.WpAssertions.assertThrowsWpNotFound;
import static io.github.evisentin.wordpress.rest.client.contract.test.WpAssertions.assertThrowsWpUnauthorized;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpCommentStatus.APPROVED;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext.EDIT;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.PRIVATE;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus.PUBLISH;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection.ASC;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpTaxonomy.CATEGORY;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpTaxonomy.POST_TAG;
import static java.util.Collections.emptySet;

public abstract class AbstractBasicAuthenticationWpRestClientContractTest extends AbstractMockServerTest {
    private WpRestClient client;

    @BeforeEach
    void setUp() {
        this.client = client();
    }

    protected abstract WpBaseRestClient client();

    private static String toBlock(final @NonNull String text) {
        return String.format("<p>%s</p>\n", text);
    }

    @DisplayName("'POST REVISION' Operations")
    @Nested
    class PostRevisionsTests {

        @DisplayName("'GET' fails on forbidden")
        @Test
        @SneakyThrows
        void getFailsOnForbidden() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/get.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.postRevisions().get(30L, 1L));
        }

        @DisplayName("'GET' fails on post not found")
        @Test
        @SneakyThrows
        void getFailsOnPostNotFound() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/get.failure.post-not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.postRevisions().get(30L, 1L), "rest_post_invalid_parent", "Invalid post parent ID.");
        }

        @DisplayName("'GET' fails on post review not found")
        @Test
        @SneakyThrows
        void getFailsOnPostReviewNotFound() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/get.failure.revision-not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.postRevisions().get(30L, 1L), "rest_post_invalid_id", "Invalid revision ID.");
        }

        @DisplayName("'GET' fails on unauthorized")
        @Test
        @SneakyThrows
        void getFailsOnUnauthorized() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.postRevisions().get(30L, 1L));
        }

        @DisplayName("'GET' succeeds")
        @Test
        @SneakyThrows
        void getSucceeds() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/get.success.json");

            // WHEN/THEN
            final WpPostRevision postRevision = client.postRevisions().get(30L, 1L);

            assertThat(postRevision).isNotNull();
            assertThat(postRevision.getParentId()).isNotNull().isEqualByComparingTo(30L);
            assertThat(postRevision.getId()).isNotNull().isEqualByComparingTo(1L);
        }

        @DisplayName("'LIST' fails on forbidden")
        @Test
        @SneakyThrows
        void listFailsOnForbidden() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.postRevisions().list(1L, new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' fails on null or blank parameters")
        @Test
        @SneakyThrows
        void listFailsOnNullParameters() {
            assertThatThrownBy(() -> client.postRevisions().list(1L, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("paginationQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on post not found")
        @Test
        @SneakyThrows
        void listFailsOnPostNotFound() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/list.failure.post-not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.postRevisions().list(1L, new WpPaginationQuery(1, 10), null), "rest_post_invalid_parent", "Invalid post parent ID.");
        }

        @DisplayName("'LIST' fails on unauthorized")
        @Test
        @SneakyThrows
        void listFailsOnUnauthorized() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.postRevisions().list(1L, new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' succeeds")
        @Test
        @SneakyThrows
        void listSucceeds() {
            // GIVEN
            givenExpectationFromFile("basic-auth/post-revisions/list.success.json");

            // WHEN/THEN
            final WpPagedResponse<WpPostRevision> response = client.postRevisions().list(1L, new WpPaginationQuery(1, 10), null);

            WordPressAssertions.assertThat(response)
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)
                               .doesNotHaveNextPage();
        }
    }

    @DisplayName("'MEDIA' Operations")
    @Nested
    class MediaTests {

        @DisplayName("'CREATE' fails on null or blank parameters")
        @Test
        @SneakyThrows
        void createFailsOnNullParameters() {
            assertThatThrownBy(() -> client.media().create(null, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("file is marked non-null but is null");

            assertThatThrownBy(() -> client.media().create(new File(""), null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("fileName is marked non-null but is null");

            assertThatThrownBy(() -> client.media().create(new File(""), "some name", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("mimeType is marked non-null but is null");

            assertThatThrownBy(() -> client.media().create(new File(""), " ", " "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("fileName cannot be blank");

            assertThatThrownBy(() -> client.media().create(new File(""), "some name", " "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("mimeType cannot be blank");
        }

        @DisplayName("'CREATE' works")
        @Test
        @SneakyThrows
        void createWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/create.success.json");

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("files/sample.png");

            Path tempFile = Files.createTempFile("sample", ".png");

            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            File file = tempFile.toFile();

            // WHEN
            final WpMedia media = client.media().create(file, "sample.png", "image/png");

            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull()
                               .hasId(9L)
                               .hasStatus(WpMediaStatus.INHERIT)
                               .hasSlug("sample-1")
                               .hasType("attachment")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw("sample-1")
                                        .hasRendered("sample-1"))
                               .hasAuthorId(1L)
                               .hasCommentStatus(WpOpenClosed.OPEN)
                               .hasPingStatus(WpOpenClosed.CLOSED)
                               .hasMediaType(WpMediaType.IMAGE)
                               .hasMimeType("image/png");
        }

        @DisplayName("'DELETE' fails on HTTP FORBIDDEN")
        @Test
        void deleteFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/delete.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.media().delete(9L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.media().delete(9L));
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.media().delete(9L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/delete.success.json");

            // WHEN
            final WpMediaDeletionResponse response = client.media().delete(9L);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(9L)
                               );
        }

        @DisplayName("'GET' fails on HTTP FORBIDDEN")
        @Test
        void getFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/get.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.media().get(9L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.media().get(9L, null));
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.media().get(9L, null));
        }

        @DisplayName("'GET' works with context")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/get.success.with-context.json");

            final long id = 9L;

            // WHEN
            final WpMedia media = client.media().get(id, WpContext.VIEW);

            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull()
                               .hasId(id);
        }

        @DisplayName("'GET' works without context")
        @Test
        void getWorksWithoutContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/get.success.without-context.json");

            final long id = 9L;

            // WHEN
            final WpMedia media = client.media().get(id, null);

            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull()
                               .hasId(id);
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.media().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.media().list(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("paginationQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.media().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void listWorksWithJustPaging() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/list.success.just-paging.json");

            // WHEN
            final WpPagedResponse<WpMedia> response = client.media().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       assertThat(r.getItems())
                                               .hasSize(1)
                                               .satisfiesExactly(
                                                       media ->
                                                               WordPressAssertions.assertThat(media)
                                                                                  .isNotNull()
                                                                                  .hasId(9L)
                                                                                  .hasTitleSatisfying(title -> title.hasRaw("sample-1")
                                                                                                                    .hasRendered("sample-1"))
                                               ));
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void listWorksWithPagingAndQuery() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/list.success.paging-and-query.json");

            final WpMediaQuery mediaQuery = WpMediaQuery.builder()
                                                        .withSlug("slug-1")
                                                        .build();

            // WHEN
            final WpPagedResponse<WpMedia> response = client.media().list(new WpPaginationQuery(1, 10), mediaQuery);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       assertThat(r.getItems())
                                               .hasSize(1)
                                               .satisfiesExactly(
                                                       media ->
                                                               WordPressAssertions.assertThat(media)
                                                                                  .isNotNull()
                                                                                  .hasId(9L)
                                                                                  .hasTitleSatisfying(title -> title.hasRaw("sample-1")
                                                                                                                    .hasRendered("sample-1"))
                                               ));
        }

        @DisplayName("'UPDATE' fails on HTTP BAD REQUEST")
        @Test
        void updateFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/update.failure.bad-request.json");

            final WpMediaUpdateRequest updateRequest =
                    WpMediaUpdateRequest.builder()
                                        .withCaption("Caption updated")
                                        .withDescription("Description updated")
                                        .withTitle("Title updated")
                                        .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.media().update(9L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP FORBIDDEN")
        @Test
        void updateFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/update.failure.forbidden.json");

            final WpMediaUpdateRequest updateRequest =
                    WpMediaUpdateRequest.builder()
                                        .withCaption("Caption updated")
                                        .withDescription("Description updated")
                                        .withTitle("Title updated")
                                        .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.media().update(9L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void updateFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/update.failure.not-found.json");

            final WpMediaUpdateRequest updateRequest =
                    WpMediaUpdateRequest.builder()
                                        .withCaption("Caption updated")
                                        .withDescription("Description updated")
                                        .withTitle("Title updated")
                                        .build();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.media().update(9L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.media().update(1000L, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("updateRequest is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on HTTP UNAUTHORIZED")
        @Test
        void updateFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/update.failure.unauthorized.json");

            final WpMediaUpdateRequest updateRequest =
                    WpMediaUpdateRequest.builder()
                                        .withCaption("Caption updated")
                                        .withDescription("Description updated")
                                        .withTitle("Title updated")
                                        .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.media().update(9L, updateRequest));
        }

        @DisplayName("UPDATE' works")
        @Test
        void updateWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/media/update.success.json");

            final WpMediaUpdateRequest updateRequest =
                    WpMediaUpdateRequest.builder()
                                        .withCaption("Caption updated")
                                        .withDescription("Description updated")
                                        .withTitle("Title updated")
                                        .build();

            // WHEN
            final WpMedia media = client.media().update(9L, updateRequest);

            WordPressAssertions.assertThat(media)
                               .isNotNull()
                               .hasId(9L)
                               .hasCaptionSatisfying(caption -> caption.hasRaw("Caption updated").hasRendered("Caption updated"))
                               .hasDescriptionSatisfying(description -> description.hasRaw("Description updated").hasRendered("Description updated"))
                               .hasTitleSatisfying(title -> title.hasRaw("Title updated").hasRendered("Title updated"));
        }
    }

    @DisplayName("'CATEGORY' Operations")
    @Nested
    class CategoryTests {

        private static final String CATEGORY_NAME = "my category";
        private static final String CATEGORY_DESCRIPTION = "my category description";
        private static final String CATEGORY_SLUG = "my-category";

        @DisplayName("'CREATE' fails on HTTP BAD REQUEST")
        @Test
        void createFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/create.failure.bad-request.json");

            final WpCategoryCreateUpdateRequest createRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(CATEGORY_NAME)
                                                 .withDescription(CATEGORY_DESCRIPTION)
                                                 .withSlug(CATEGORY_SLUG)
                                                 .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.categories().create(createRequest));
        }

        @DisplayName("'CREATE' fails on blank name")
        @Test
        void createFailsOnBlankName() {

            // GIVEN
            final WpCategoryCreateUpdateRequest createRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName("   ")
                                                 .withDescription(CATEGORY_DESCRIPTION)
                                                 .withSlug(CATEGORY_SLUG)
                                                 .build();

            // WHEN/THEN
            assertThatThrownBy(() -> client.categories().create(createRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name cannot be blank");
        }

        @DisplayName("'CREATE' fails on HTTP FORBIDDEN")
        @Test
        void createFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/create.failure.forbidden.json");

            final WpCategoryCreateUpdateRequest createRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(CATEGORY_NAME)
                                                 .withDescription(CATEGORY_DESCRIPTION)
                                                 .withSlug(CATEGORY_SLUG)
                                                 .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.categories().create(createRequest));
        }

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.categories().create(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");
        }

        @DisplayName("'CREATE' fails on HTTP UNAUTHORIZED")
        @Test
        void createFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/create.failure.unauthorized.json");

            final WpCategoryCreateUpdateRequest createRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(CATEGORY_NAME)
                                                 .withDescription(CATEGORY_DESCRIPTION)
                                                 .withSlug(CATEGORY_SLUG)
                                                 .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.categories().create(createRequest));
        }

        @DisplayName("'CREATE' works")
        @Test
        void createWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/create.success.json");

            final String PARENT_SLUG = "top-category";
            final Long parent_id = 2L;

            // WHEN
            final WpCategoryCreateUpdateRequest createRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(CATEGORY_NAME)
                                                 .withDescription(CATEGORY_DESCRIPTION)
                                                 .withSlug(CATEGORY_SLUG)
                                                 .withParentId(parent_id)
                                                 .build();

            final WpCategory category = client.categories().create(createRequest);

            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasParentId(parent_id)
                               .hasCount(0)
                               .hasDescription(CATEGORY_DESCRIPTION)
                               .hasName(CATEGORY_NAME).hasSlug(CATEGORY_SLUG)
                               .hasTaxonomy(CATEGORY)
                               .satisfies(cat -> {
                                           assertThat(cat.getId()).isNotNull();
                                           assertThat(cat.getLink()).isNotBlank().contains(PARENT_SLUG, CATEGORY_SLUG);
                                       }
                               );
        }

        @DisplayName("'DELETE' fails on HTTP FORBIDDEN")
        @Test
        void deleteFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/delete.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.categories().delete(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.categories().delete(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.categories().delete(1005L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/delete.success.json");

            // WHEN
            final WpCategoryDeletionResponse response = client.categories().delete(1005L);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(1005L)
                                              .hasCount(0)
                                              .hasDescription(CATEGORY_DESCRIPTION)
                                              .hasName(CATEGORY_NAME)
                                              .hasSlug(CATEGORY_SLUG)
                                              .hasTaxonomy(CATEGORY)

                               );
        }

        @DisplayName("'GET' fails on HTTP FORBIDDEN")
        @Test
        void getFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/get.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.categories().get(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.categories().get(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.categories().get(1005L, null));
        }

        @DisplayName("'GET' works with context")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/get.success.with-context.json");

            final long catId = 2L;

            // WHEN
            final WpCategory category = client.categories().get(catId, WpContext.VIEW);

            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(catId)
                               .hasCount(0)
                               .hasDescription(CATEGORY_DESCRIPTION)
                               .hasName(CATEGORY_NAME)
                               .hasSlug(CATEGORY_SLUG)
                               .hasTaxonomy(CATEGORY);
        }

        @DisplayName("'GET' works without context")
        @Test
        void getWorksWithoutContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/get.success.without-context.json");

            final long catId = 2L;

            // WHEN
            final WpCategory category = client.categories().get(catId, null);

            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(catId)
                               .hasCount(0)
                               .hasDescription(CATEGORY_DESCRIPTION)
                               .hasName(CATEGORY_NAME)
                               .hasSlug(CATEGORY_SLUG)
                               .hasTaxonomy(CATEGORY);
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.categories().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.categories().list(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("paginationQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.categories().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void listWorksWithJustPaging() {

            // GIVEN
            final String NAME_1 = "category1";
            final String DESCRIPTION_1 = "Category #1";
            final String SLUG_1 = "category-1";

            final String NAME_2 = "category2";
            final String DESCRIPTION_2 = "Category #2";
            final String SLUG_2 = "category-2";

            givenExpectationFromFile("basic-auth/category/list.success.just-paging.json");

            // WHEN
            final WpPagedResponse<WpCategory> response = client.categories().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(3) // 2 items created here, plus the default category (id=1) which cannot be deleted
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       assertThat(r.getItems())
                                               .hasSize(3)
                                               .satisfiesExactly(
                                                       cat -> WordPressAssertions.assertThat(cat)
                                                                                 .hasId(2L)
                                                                                 .hasName(NAME_1)
                                                                                 .hasSlug(SLUG_1)
                                                                                 .hasDescription(DESCRIPTION_1)
                                                                                 .hasTaxonomy(CATEGORY),
                                                       cat -> WordPressAssertions.assertThat(cat)
                                                                                 .hasId(3L)
                                                                                 .hasName(NAME_2)
                                                                                 .hasSlug(SLUG_2)
                                                                                 .hasDescription(DESCRIPTION_2)
                                                                                 .hasTaxonomy(CATEGORY),
                                                       cat -> WordPressAssertions.assertThat(cat)
                                                                                 .hasId(1L)
                                                                                 .hasName("Uncategorized")
                                                                                 .hasSlug("uncategorized")
                                                                                 .hasDescription("")
                                                                                 .hasTaxonomy(CATEGORY)

                                               ));
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void listWorksWithPagingAndQuery() {

            // GIVEN
            final String NAME_2 = "category2";
            final String DESCRIPTION_2 = "Category #2";
            final String SLUG_2 = "category-2";

            givenExpectationFromFile("basic-auth/category/list.success.paging-and-query.json");

            // Filter just for category #2
            final WpCategoryQuery categoryQuery = WpCategoryQuery.builder()
                                                                 .withSlug(SLUG_2)
                                                                 .build();

            // WHEN
            final WpPagedResponse<WpCategory> response = client.categories().list(new WpPaginationQuery(1, 10), categoryQuery);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       assertThat(r.getItems())
                                               .hasSize(1)
                                               .satisfiesExactly(
                                                       cat -> WordPressAssertions.assertThat(cat)
                                                                                 .hasId(3L)
                                                                                 .hasName(NAME_2)
                                                                                 .hasSlug(SLUG_2)
                                                                                 .hasDescription(DESCRIPTION_2)
                                                                                 .hasTaxonomy(CATEGORY)

                                               ));
        }

        @DisplayName("'UPDATE' fails on HTTP BAD REQUEST")
        @Test
        void updateFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/update.failure.bad-request.json");

            final String NAME = "cat1_updated";
            final String DESCRIPTION = "Category #1_updated";
            final String SLUG = "cat-1updated";

            final WpCategoryCreateUpdateRequest updateRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(NAME)
                                                 .withDescription(DESCRIPTION)
                                                 .withSlug(SLUG)
                                                 .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.categories().update(2L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP FORBIDDEN")
        @Test
        void updateFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/update.failure.forbidden.json");

            final String NAME = "cat1_updated";
            final String DESCRIPTION = "Category #1_updated";
            final String SLUG = "cat-1updated";

            final WpCategoryCreateUpdateRequest updateRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(NAME)
                                                 .withDescription(DESCRIPTION)
                                                 .withSlug(SLUG)
                                                 .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.categories().update(2L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void updateFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/update.failure.not-found.json");

            final String NAME = "cat1_updated";
            final String DESCRIPTION = "Category #1_updated";
            final String SLUG = "cat-1updated";

            final WpCategoryCreateUpdateRequest updateRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(NAME)
                                                 .withDescription(DESCRIPTION)
                                                 .withSlug(SLUG)
                                                 .build();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.categories().update(2L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.categories().update(1000L, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("updateRequest is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on HTTP UNAUTHORIZED")
        @Test
        void updateFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/update.failure.unauthorized.json");

            final String NAME = "cat1_updated";
            final String DESCRIPTION = "Category #1_updated";
            final String SLUG = "cat-1updated";

            final WpCategoryCreateUpdateRequest updateRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(NAME)
                                                 .withDescription(DESCRIPTION)
                                                 .withSlug(SLUG)
                                                 .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.categories().update(2L, updateRequest));
        }

        @DisplayName("UPDATE' works")
        @Test
        void updateWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/update.success.json");

            final String CAT_1_UPDATED_NAME = "Category #1 UPDATED";
            final String CAT_1_UPDATED_DESCRIPTION = "Category 1 UPDATED";
            final String CAT_1_UPDATED_SLUG = "cat-1-updated";

            WpCategoryCreateUpdateRequest updateRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(CAT_1_UPDATED_NAME)
                                                 .withDescription(CAT_1_UPDATED_DESCRIPTION)
                                                 .withSlug(CAT_1_UPDATED_SLUG)
                                                 .build();

            // WHEN
            final WpCategory category = client.categories().update(2L, updateRequest);

            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(2L)
                               .hasCount(0)
                               .hasDescription(CAT_1_UPDATED_DESCRIPTION)
                               .hasName(CAT_1_UPDATED_NAME)
                               .hasSlug(CAT_1_UPDATED_SLUG)
                               .hasNoParent()
                               .hasTaxonomy(CATEGORY);
        }
    }

    @DisplayName("'COMMENT' Operations")
    @Nested
    class CommentTests {

        @DisplayName("'CREATE' fails on HTTP BAD REQUEST")
        @Test
        void createFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/create.failure.bad-request.json");

            final String CONTENT = "My Content";

            final WpCommentCreateUpdateRequest createRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(CONTENT)
                                                .withPostId(8L)
                                                .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.comments().create(createRequest));
        }

        @DisplayName("'CREATE' fails on blank content")

        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        void createFailsOnBlankContent(String content) {

            final WpCommentCreateUpdateRequest createRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(content)
                                                .withPostId(8L)
                                                .build();

            // WHEN/THEN
            assertThatThrownBy(() -> client.comments().create(createRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("content cannot be blank");
        }

        @DisplayName("'CREATE' fails on HTTP FORBIDDEN")
        @Test
        void createFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/create.failure.forbidden.json");

            final String CONTENT = "My Content";

            final WpCommentCreateUpdateRequest createRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(CONTENT)
                                                .withPostId(8L)
                                                .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.comments().create(createRequest));
        }

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.comments().create(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");
        }

        @DisplayName("'CREATE' fails on HTTP UNAUTHORIZED")
        @Test
        void createFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/create.failure.unauthorized.json");

            final String CONTENT = "My Content";

            final WpCommentCreateUpdateRequest createRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(CONTENT)
                                                .withPostId(8L)
                                                .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.comments().create(createRequest));
        }

        @DisplayName("'CREATE' works")
        @Test
        void createWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/create.success.json");

            // WHEN

            WpCommentCreateUpdateRequest createUpdateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withPostId(8L)
                                                .withAuthorName("admin")
                                                .withAuthorIp("10.0.0.1")
                                                .withAuthorId(1L)
                                                .withAuthorEmail("a@a.com")
                                                .withAuthorUrl("http://aa.com")
                                                .withStatus(APPROVED)
                                                .withContent("This is a test comment created via the WordPress REST API")
                                                .withDate(LocalDateTime.of(2025, 8, 7, 10, 30, 0))
                                                .withDateGMT(LocalDateTime.of(2025, 8, 7, 8, 30, 0))
                                                .build();

            final WpComment comment = client.comments().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId(10L)
                               .hasPost(8L)
                               .hasParent(0L)
                               .hasAuthorId(1L)
                               .hasAuthorName("admin")
                               .hasStatus(APPROVED)
                               .hasContentSatisfying(c -> c.hasRaw("This is a test comment created via the WordPress REST API"));
        }

        @DisplayName("'CREATE' works with password")
        @Test
        void createWorksWithPassword() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/create.success.with-password.json");

            // WHEN

            WpCommentCreateUpdateRequest createUpdateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withPostId(8L)
                                                .withAuthorName("admin")
                                                .withAuthorIp("10.0.0.1")
                                                .withAuthorId(1L)
                                                .withAuthorEmail("a@a.com")
                                                .withAuthorUrl("http://aa.com")
                                                .withStatus(APPROVED)
                                                .withContent("This is a test comment created via the WordPress REST API")
                                                .withDate(LocalDateTime.of(2025, 8, 7, 10, 30, 0))
                                                .withDateGMT(LocalDateTime.of(2025, 8, 7, 8, 30, 0))
                                                .withPassword("my-password")
                                                .build();

            final WpComment comment = client.comments().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId(12L)
                               .hasPost(8L)
                               .hasParent(0L)
                               .hasAuthorId(1L)
                               .hasAuthorName("admin")
                               .hasStatus(APPROVED)
                               .hasContentSatisfying(c -> c.hasRaw("This is a test comment created via the WordPress REST API"));
        }

        @DisplayName("'DELETE' fails on HTTP FORBIDDEN")
        @Test
        void deleteFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/delete.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.comments().delete(13L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.comments().delete(13L));
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.comments().delete(13L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/delete.success.json");

            // WHEN
            final WpCommentDeletionResponse response = client.comments().delete(13L);

            // THEN

            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(13L)
                                              .hasStatus(APPROVED)
                               );
        }

        @DisplayName("'GET' fails on HTTP FORBIDDEN")
        @Test
        void getFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/get.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.comments().get(12L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.comments().get(12L, null));
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.comments().get(12L, null));
        }

        @DisplayName("'GET' works (with context)")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/get.success.with-context.json");

            final long id = 12L;

            // WHEN
            final WpComment comment = client.comments().get(id, EDIT);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(APPROVED);
        }

        @DisplayName("'GET' works (with context and password)")
        @Test
        void getWorksWithContextAndPassword() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/get.success.with-context-and-password.json");

            final long id = 12L;

            // WHEN
            final WpComment comment = client.comments().get(id, EDIT, "my-password");

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(APPROVED);
        }

        @DisplayName("'GET' works (with no context - default)")
        @Test
        void getWorksWithNoContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/get.success.without-context.json");

            final long id = 12L;

            // WHEN
            final WpComment comment = client.comments().get(id, null);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(APPROVED);
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.comments().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.comments().list(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("paginationQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.comments().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void listWorksWithJustPaging() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/list.success.just-paging.json");

            // WHEN
            final WpPagedResponse<WpComment> response = client.comments().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       WordPressAssertions.assertThat(r.getItems().get(0))
                                                          .isNotNull()
                                                          .hasId(4L)
                                                          .hasPost(8L)
                                                          .hasAuthorId(1L)
                                                          .hasAuthorName("admin")
                                                          .hasAuthorUrl("http://localhost:8080")
                                                          .hasLink("http://localhost:8080/archives/8#comment-4")
                                                          .hasStatus(APPROVED)
                                                          .hasType("comment")
                               ).satisfies(r ->
                                       WordPressAssertions.assertThat(r.getItems().get(1))
                                                          .isNotNull()
                                                          .hasId(3L)
                                                          .hasPost(8L)
                                                          .hasAuthorId(1L)
                                                          .hasAuthorName("admin")
                                                          .hasAuthorUrl("http://localhost:8080")
                                                          .hasLink("http://localhost:8080/archives/8#comment-3")
                                                          .hasStatus(APPROVED)
                                                          .hasType("comment")
                               );
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void listWorksWithPagingAndQuery() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/list.success.paging-and-query.json");

            // WHEN
            final WpCommentQuery commentQuery = WpCommentQuery.builder()
                                                              .withStatus(APPROVED)
                                                              .withOrder(ASC)
                                                              .withOrderBy(WpCommentOrderFields.ID)
                                                              .build();

            // WHEN
            final WpPagedResponse<WpComment> response = client.comments().list(new WpPaginationQuery(1, 10), commentQuery);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)
                               .doesNotHaveNextPage()

                               .satisfies(r ->
                                       WordPressAssertions.assertThat(r.getItems().get(0))
                                                          .isNotNull()
                                                          .hasId(3L)
                                                          .hasPost(8L)
                                                          .hasAuthorId(1L)
                                                          .hasAuthorName("admin")
                                                          .hasAuthorUrl("http://localhost:8080")
                                                          .hasLink("http://localhost:8080/archives/8#comment-3")
                                                          .hasStatus(APPROVED)
                                                          .hasType("comment")
                               )
                               .satisfies(r ->
                                       WordPressAssertions.assertThat(r.getItems().get(1))
                                                          .isNotNull()
                                                          .hasId(4L)
                                                          .hasPost(8L)
                                                          .hasAuthorId(1L)
                                                          .hasAuthorName("admin")
                                                          .hasAuthorUrl("http://localhost:8080")
                                                          .hasLink("http://localhost:8080/archives/8#comment-4")
                                                          .hasStatus(APPROVED)
                                                          .hasType("comment")
                               );
        }

        @DisplayName("'TRASH' fails on HTTP FORBIDDEN")
        @Test
        void trashFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/trash.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.comments().trash(12L));
        }

        @DisplayName("'TRASH' fails on HTTP NOT FOUND")
        @Test
        void trashFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/trash.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.comments().trash(12L));
        }

        @DisplayName("'TRASH' fails on HTTP UNAUTHORIZED")
        @Test
        void trashFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/trash.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.comments().trash(12L));
        }

        @DisplayName("'TRASH' works")
        @Test
        void trashWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/trash.success.json");

            // WHEN
            final WpComment comment = client.comments().trash(12L);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId(12L)
                               .hasStatus(WpCommentStatus.TRASH);
        }

        @DisplayName("'UPDATE' fails on HTTP BAD REQUEST")
        @Test
        void updateFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/update.failure.bad-request.json");

            final String CONTENT = "My Content";

            WpCommentCreateUpdateRequest updateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(CONTENT + " UPDATED")
                                                .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.comments().update(12L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP FORBIDDEN")
        @Test
        void updateFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/update.failure.forbidden.json");

            final String CONTENT = "My Content";

            WpCommentCreateUpdateRequest updateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(CONTENT + " UPDATED")
                                                .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.comments().update(12L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void updateFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/update.failure.not-found.json");

            final String CONTENT = "My Content";

            WpCommentCreateUpdateRequest updateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(CONTENT + " UPDATED")
                                                .build();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.comments().update(12L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.comments().update(1000L, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("updateRequest is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on HTTP UNAUTHORIZED")
        @Test
        void updateFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/update.failure.unauthorized.json");
            final String CONTENT = "My Content";

            WpCommentCreateUpdateRequest updateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(CONTENT + " UPDATED")
                                                .build();
            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.comments().update(12L, updateRequest));
        }

        @DisplayName("'UPDATE' works")
        @Test
        void updateWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/comment/update.success.json");

            final String CONTENT = "My Content";

            final String CONTENT_UPDATED = CONTENT + " UPDATED";
            WpCommentCreateUpdateRequest updateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withContent(CONTENT_UPDATED)
                                                .build();

            // WHEN
            val comment = client.comments().update(12L, updateRequest);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId(12L)
                               .hasPost(8L)
                               .hasParent(0L)
                               .hasAuthorId(1L)
                               .hasAuthorName("admin")
                               .hasStatus(APPROVED)
                               .hasContentSatisfying(c -> c.hasRaw(CONTENT_UPDATED));
        }
    }

    @DisplayName("'PAGE' Operations")
    @Nested
    class PageTests {

        @DisplayName("'CREATE' fails on HTTP BAD REQUEST")
        @Test
        void createFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/create.failure.bad-request.json");

            final String TITLE = "My page";
            final String CONTENT = "My Content";

            final WpPageCreateUpdateRequest createRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT).build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.pages().create(createRequest));
        }

        @DisplayName("'CREATE' fails on HTTP FORBIDDEN")
        @Test
        void createFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/create.failure.forbidden.json");

            final String TITLE = "My page";
            final String CONTENT = "My Content";

            final WpPageCreateUpdateRequest createRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.pages().create(createRequest));
        }

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.pages().create(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");

            assertThatThrownBy(() -> client.pages().create(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");
        }

        @DisplayName("'CREATE' fails on HTTP UNAUTHORIZED")
        @Test
        void createFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/create.failure.unauthorized.json");

            final String TITLE = "My page";
            final String CONTENT = "My Content";

            final WpPageCreateUpdateRequest createRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.pages().create(createRequest));
        }

        @DisplayName("'CREATE' works")
        @Test
        void createWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/create.success.json");

            // WHEN
            final String TITLE = "Page #1";
            final String CONTENT = "My first page";

            final WpPageCreateUpdateRequest createUpdateRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .build();

            final WpPage page = client.pages().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasSlug("")
                               .hasGeneratedSlug(toWordPressSlug(TITLE))
                               .hasStatus(WpPageStatus.DRAFT)
                               .hasType("page")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(TITLE)
                                        .hasRendered(TITLE)
                               )
                               .hasContentSatisfying(c ->
                                       c.hasRaw(CONTENT)
                                        .hasRendered(toBlock(CONTENT))
                                        .isNotProtected()
                                        .hasBlockVersion(0)

                               ).hasExcerptSatisfying(e ->
                                       e.hasRaw("")
                                        .hasRendered(toBlock(CONTENT))
                                        .isNotProtected()

                               );
        }

        @DisplayName("'CREATE' works with password")
        @Test
        void createWorksWithPassword() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/create.success.with-password.json");

            // WHEN
            final String TITLE = "Page #1";
            final String CONTENT = "My first page";

            final WpPageCreateUpdateRequest createUpdateRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .withPassword("my password")
                                             .withStatus(WpPageStatus.PUBLISH)
                                             .build();

            final WpPage page = client.pages().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasSlug(toWordPressSlug(TITLE))
                               .hasGeneratedSlug(toWordPressSlug(TITLE))
                               .hasStatus(WpPageStatus.PUBLISH)
                               .hasPassword("my password")
                               .hasType("page")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(TITLE)
                                        .hasRendered(TITLE)
                               )
                               .hasContentSatisfying(c ->
                                       c.hasRaw(CONTENT)
                                        .hasRendered(toBlock(CONTENT))
                                        .isProtected()
                                        .hasBlockVersion(0)

                               ).hasExcerptSatisfying(e ->
                                       e.hasRaw("")
                                        .hasRendered(toBlock(CONTENT))
                                        .isProtected()

                               );
        }

        @DisplayName("'DELETE' fails on HTTP FORBIDDEN")
        @Test
        void deleteFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/delete.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.pages().delete(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.pages().delete(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.pages().delete(1005L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/delete.success.json");

            // WHEN
            final WpPageDeletionResponse response = client.pages().delete(1005L);

            // THEN

            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(1005L)
                                              .hasSlug("page-1")
                                              .hasTitleSatisfying(title ->
                                                      title.hasRaw("Page #1")
                                                           .hasRendered("Page #1"))
                                              .hasContentSatisfying(content ->
                                                      content.hasRaw("My first page")
                                                             .hasRendered(toBlock("My first page")))
                                              .hasExcerptSatisfying(excerpt ->
                                                      excerpt.hasRaw("")
                                                             .hasRendered(toBlock("My first page")))
                               );
        }

        @DisplayName("'GET' fails on HTTP FORBIDDEN")
        @Test
        void getFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/get.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.pages().get(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.pages().get(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.pages().get(1005L, null));
        }

        @DisplayName("'GET' works (with context)")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/get.success.with-context.json");

            final long id = 39L;

            // WHEN
            final WpPage page = client.pages().get(id, EDIT);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(WpPageStatus.PUBLISH);
        }

        @DisplayName("'GET' works (with context and password)")
        @Test
        void getWorksWithContextAndPassword() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/get.success.with-context-and-password.json");

            final long id = 40L;

            // WHEN
            final WpPage page = client.pages().get(id, EDIT, "my-password");

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(WpPageStatus.PUBLISH);
        }

        @DisplayName("'GET' works (with no context - default)")
        @Test
        void getWorksWithNoContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/get.success.without-context.json");

            final long id = 43L;

            // WHEN
            final WpPage page = client.pages().get(id, null);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(WpPageStatus.PUBLISH);
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.pages().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.pages().list(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("paginationQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.pages().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void listWorksWithJustPaging() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/list.success.just-paging.json");

            // WHEN
            final WpPagedResponse<WpPage> response = client.pages().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       WordPressAssertions.assertThat(r.getItems().get(0))
                                                          .isNotNull()
                                                          .hasId(39L)
                                                          .hasLink("https://localhost:63870/page-1/")
                                                          .hasSlug("page-1")
                                                          .hasStatus(WpPageStatus.PUBLISH)
                                                          .hasType("page")
                                                          .hasTitleSatisfying(title ->
                                                                  title.hasRaw(null) // during listing we have just "rendered", not "raw"
                                                                       .hasRendered("Page #1"))
                                                          .hasContentSatisfying(title ->
                                                                  title.hasRaw(null) // during listing we have just "rendered", not "raw"
                                                                       .hasRendered("<p>My first page</p>\n"))
                                                          .hasExcerptSatisfying(excerpt ->
                                                                  excerpt.hasRaw(null) // during listing we have just "rendered", not "raw"
                                                                         .hasRendered("<p>My first page</p>\n")));
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void listWorksWithPagingAndQuery() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/list.success.paging-and-query.json");

            // WHEN
            final WpPageQuery query = WpPageQuery.builder()
                                                 .withStatus(WpPageStatus.DRAFT)
                                                 .withStatus(WpPageStatus.PRIVATE)
                                                 .withOrder(ASC)
                                                 .withOrderBy(WpPageOrderFields.ID)
                                                 .build();

            final WpPagedResponse<WpPage> response = client.pages().list(new WpPaginationQuery(1, 10), query);

            // THEN

            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)
                               .doesNotHaveNextPage()
                               .satisfies(r -> {
                                   WordPressAssertions.assertThat(r.getItems().get(0))
                                                      .isNotNull()
                                                      .hasId(36L)
                                                      .hasStatus(WpPageStatus.DRAFT);
                                   WordPressAssertions.assertThat(r.getItems().get(1))
                                                      .isNotNull()
                                                      .hasId(37L)
                                                      .hasStatus(WpPageStatus.PRIVATE);
                               });
        }

        @DisplayName("'TRASH' fails on HTTP FORBIDDEN")
        @Test
        void trashFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/trash.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.pages().trash(1005L));
        }

        @DisplayName("'TRASH' fails on HTTP NOT FOUND")
        @Test
        void trashFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/trash.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.pages().trash(1005L));
        }

        @DisplayName("'TRASH' fails on HTTP UNAUTHORIZED")
        @Test
        void trashFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/trash.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.pages().trash(1005L));
        }

        @DisplayName("'TRASH' works")
        @Test
        void trashWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/trash.success.json");

            // WHEN
            final WpPage page = client.pages().trash(45L);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasStatus(WpPageStatus.TRASH);
        }

        @DisplayName("'UPDATE' fails on HTTP BAD REQUEST")
        @Test
        void updateFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/update.failure.bad-request.json");

            final String TITLE = "My page";
            final String CONTENT = "My Content";

            WpPageCreateUpdateRequest updateRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(WpPageStatus.DRAFT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.pages().update(4L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP FORBIDDEN")
        @Test
        void updateFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/update.failure.forbidden.json");

            final String TITLE = "My page";
            final String CONTENT = "My Content";

            WpPageCreateUpdateRequest updateRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(WpPageStatus.DRAFT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.pages().update(4L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void updateFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/update.failure.not-found.json");

            final String TITLE = "My page";
            final String CONTENT = "My Content";

            WpPageCreateUpdateRequest updateRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(WpPageStatus.DRAFT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.pages().update(4L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.pages().update(1000L, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("updateRequest is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on HTTP UNAUTHORIZED")
        @Test
        void updateFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/update.failure.unauthorized.json");

            final String TITLE = "My page";
            final String CONTENT = "My Content";

            WpPageCreateUpdateRequest updateRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(WpPageStatus.DRAFT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.pages().update(4L, updateRequest));
        }

        @DisplayName("'UPDATE' works")
        @Test
        void updateWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/page/update.success.json");

            final String TITLE = "Page #1";
            final String CONTENT = "My first page";

            WpPageCreateUpdateRequest updateRequest =
                    WpPageCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(WpPageStatus.DRAFT)
                                             .build();

            // WHEN
            val page = client.pages().update(39L, updateRequest);

            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId(39L)
                               .hasStatus(WpPageStatus.DRAFT)
                               .hasTitleSatisfying(title ->
                                       title.hasRaw(TITLE + " UPDATED")
                                            .hasRendered(TITLE + " UPDATED"))
                               .hasContentSatisfying(content ->
                                       content.hasRaw(CONTENT + " UPDATED")
                                              .hasRendered(toBlock(CONTENT + " UPDATED")))
                               .hasExcerptSatisfying(excerpt ->
                                       excerpt.hasRaw(CONTENT + " UPDATED")
                                              .hasRendered(toBlock(CONTENT + " UPDATED")));
        }
    }

    @DisplayName("'POST' Operations")
    @Nested
    class PostTests {

        @DisplayName("'CREATE' fails on HTTP BAD REQUEST")
        @Test
        void createFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/create.failure.bad-request.json");

            final String TITLE = "My post";
            final String CONTENT = "My Content";

            final WpPostCreateUpdateRequest createRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .withSticky(false)
                                             .withCategory(3L)
                                             .withTag(2L)
                                             .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.posts().create(createRequest));
        }

        @DisplayName("'CREATE' fails on HTTP FORBIDDEN")
        @Test
        void createFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/create.failure.forbidden.json");

            final String TITLE = "My post";
            final String CONTENT = "My Content";

            final WpPostCreateUpdateRequest createRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .withSticky(false)
                                             .withCategory(3L)
                                             .withTag(2L)
                                             .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.posts().create(createRequest));
        }

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.posts().create(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");

            assertThatThrownBy(() -> client.posts().create(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");
        }

        @DisplayName("'CREATE' fails on HTTP UNAUTHORIZED")
        @Test
        void createFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/create.failure.unauthorized.json");

            final String TITLE = "My post";
            final String CONTENT = "My Content";

            final WpPostCreateUpdateRequest createRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .withSticky(false)
                                             .withCategory(3L)
                                             .withTag(2L)
                                             .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.posts().create(createRequest));
        }

        @DisplayName("'CREATE' works")
        @Test
        void createWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/create.success.json");

            // WHEN
            final String TITLE = "My post";
            final String CONTENT = "My Content";

            WpPostCreateUpdateRequest createUpdateRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .withCategory(3L)
                                             .withTag(2L)
                                             .build();

            final WpPost post = client.posts().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasSlug("")
                               .hasGeneratedSlug(toWordPressSlug(TITLE))
                               .hasStatus(DRAFT)
                               .hasType("post")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(TITLE)
                                        .hasRendered(TITLE)
                               )
                               .hasContentSatisfying(c ->
                                       c.hasRaw(CONTENT)
                                        .hasRendered(toBlock(CONTENT))
                                        .isNotProtected()
                                        .hasBlockVersion(0)

                               ).hasExcerptSatisfying(e ->
                                       e.hasRaw("")
                                        .hasRendered(toBlock(CONTENT))
                                        .isNotProtected()

                               )
                               .hasCategories(Set.of(3L))
                               .hasTags(Set.of(2L));
        }

        @DisplayName("'CREATE' works with password")
        @Test
        void createWorksWithPassword() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/create.success.with-password.json");

            // WHEN
            final String TITLE = "My post";
            final String CONTENT = "My Content";

            WpPostCreateUpdateRequest createUpdateRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE)
                                             .withContent(CONTENT)
                                             .withExcerpt(CONTENT)
                                             .withPassword("my password")
                                             .withStatus(PUBLISH)
                                             .build();

            final WpPost post = client.posts().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasSlug(toWordPressSlug(TITLE))
                               .hasGeneratedSlug(toWordPressSlug(TITLE))
                               .hasStatus(PUBLISH)
                               .hasPassword("my password")
                               .hasType("post")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(TITLE)
                                        .hasRendered(TITLE)
                               )
                               .hasContentSatisfying(c ->
                                       c.hasRaw(CONTENT)
                                        .hasRendered(toBlock(CONTENT))
                                        .isProtected()
                                        .hasBlockVersion(0)

                               ).hasExcerptSatisfying(e ->
                                       e.hasRaw(CONTENT)
                                        .hasRendered(toBlock(CONTENT))
                                        .isProtected()

                               );
        }

        @DisplayName("'DELETE' fails on HTTP FORBIDDEN")
        @Test
        void deleteFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/delete.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.posts().delete(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.posts().delete(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.posts().delete(1005L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/delete.success.json");

            // WHEN
            final WpPostDeletionResponse response = client.posts().delete(1005L);

            // THEN

            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(1005L)
                                              .hasSlug("my-post")
                                              .hasTitleSatisfying(title ->
                                                      title.hasRaw("My post")
                                                           .hasRendered("My post"))
                                              .hasContentSatisfying(content ->
                                                      content.hasRaw("My Content")
                                                             .hasRendered(toBlock("My Content")))
                                              .hasExcerptSatisfying(excerpt ->
                                                      excerpt.hasRaw("My Content")
                                                             .hasRendered(toBlock("My Content")))
                               );
        }

        @DisplayName("'GET' fails on HTTP FORBIDDEN")
        @Test
        void getFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.posts().get(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.posts().get(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.posts().get(1005L, null));
        }

        @DisplayName("'GET' works (with context)")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.success.with-context.json");

            final long id = 2L;

            // WHEN
            final WpPost post = client.posts().get(id, EDIT);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(DRAFT);
        }

        @DisplayName("'GET' works (with context and password)")
        @Test
        void getWorksWithContextAndPassword() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.success.with-context-and-password.json");

            final long id = 2L;

            // WHEN
            final WpPost post = client.posts().get(id, EDIT, "my-password");

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(DRAFT);
        }

        @DisplayName("'GET' works (with no context - default)")
        @Test
        void getWorksWithNoContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.success.without-context.json");

            final long id = 2L;

            // WHEN
            final WpPost post = client.posts().get(id, null);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId(id)
                               .hasStatus(DRAFT);
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.posts().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.posts().list(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("paginationQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.posts().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void listWorksWithJustPaging() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/list.success.just-paging.json");

            // WHEN
            final WpPagedResponse<WpPost> response = client.posts().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       WordPressAssertions.assertThat(r.getItems().get(0))
                                                          .isNotNull()
                                                          .hasId(4L)
                                                          .hasLink("https://localhost:61975/my-first-post/")
                                                          .hasSlug("my-first-post")
                                                          .hasStatus(PUBLISH)
                                                          .hasType("post")
                                                          .hasCategories(Set.of(1L))
                                                          .hasTags(emptySet())
                                                          .hasTitleSatisfying(title ->
                                                                  title.hasRaw(null) // during listing we have just "rendered", not "raw"
                                                                       .hasRendered("My first post"))
                                                          .hasContentSatisfying(title ->
                                                                  title.hasRaw(null) // during listing we have just "rendered", not "raw"
                                                                       .hasRendered(""))
                                                          .hasExcerptSatisfying(excerpt ->
                                                                  excerpt.hasRaw(null) // during listing we have just "rendered", not "raw"
                                                                         .hasRendered("")));
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void listWorksWithPagingAndQuery() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/list.success.paging-and-query.json");

            // WHEN
            final WpPostQuery postQuery = WpPostQuery.builder()
                                                     .withStatus(DRAFT)
                                                     .withStatus(PRIVATE)
                                                     .withOrder(ASC)
                                                     .withOrderBy(WpPostOrderFields.ID)
                                                     .build();

            final WpPagedResponse<WpPost> response = client.posts().list(new WpPaginationQuery(1, 10), postQuery);

            // THEN

            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)
                               .doesNotHaveNextPage()
                               .satisfies(r -> {
                                   WordPressAssertions.assertThat(r.getItems().get(0))
                                                      .isNotNull()
                                                      .hasId(4L)
                                                      .hasStatus(DRAFT);
                                   WordPressAssertions.assertThat(r.getItems().get(1))
                                                      .isNotNull()
                                                      .hasId(5L)
                                                      .hasStatus(PRIVATE);
                               });
        }

        @DisplayName("'TRASH' fails on HTTP FORBIDDEN")
        @Test
        void trashFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/trash.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.posts().trash(1005L));
        }

        @DisplayName("'TRASH' fails on HTTP NOT FOUND")
        @Test
        void trashFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/trash.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.posts().trash(1005L));
        }

        @DisplayName("'TRASH' fails on HTTP UNAUTHORIZED")
        @Test
        void trashFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/trash.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.posts().trash(1005L));
        }

        @DisplayName("'TRASH' works")
        @Test
        void trashWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/trash.success.json");

            // WHEN
            final WpPost post = client.posts().trash(1005L);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasStatus(WpPostStatus.TRASH);
        }

        @DisplayName("'UPDATE' fails on HTTP BAD REQUEST")
        @Test
        void updateFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/update.failure.bad-request.json");

            final String TITLE = "My post";
            final String CONTENT = "My Content";

            WpPostCreateUpdateRequest updateRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(DRAFT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.posts().update(4L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP FORBIDDEN")
        @Test
        void updateFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/update.failure.forbidden.json");

            final String TITLE = "My post";
            final String CONTENT = "My Content";

            WpPostCreateUpdateRequest updateRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(DRAFT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.posts().update(4L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void updateFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/update.failure.not-found.json");

            final String TITLE = "My post";
            final String CONTENT = "My Content";

            WpPostCreateUpdateRequest updateRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(DRAFT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.posts().update(4L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.posts().update(1000L, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("updateRequest is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on HTTP UNAUTHORIZED")
        @Test
        void updateFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/update.failure.unauthorized.json");

            final String TITLE = "My post";
            final String CONTENT = "My Content";

            WpPostCreateUpdateRequest updateRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(DRAFT)
                                             .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.posts().update(4L, updateRequest));
        }

        @DisplayName("'UPDATE' works")
        @Test
        void updateWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/update.success.json");

            final String TITLE = "My post";
            final String CONTENT = "My Content";

            WpPostCreateUpdateRequest updateRequest =
                    WpPostCreateUpdateRequest.builder()
                                             .withTitle(TITLE + " UPDATED")
                                             .withContent(CONTENT + " UPDATED")
                                             .withExcerpt(CONTENT + " UPDATED")
                                             .withStatus(DRAFT)
                                             .build();

            // WHEN
            val post = client.posts().update(4L, updateRequest);

            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId(4L)
                               .hasStatus(DRAFT)
                               .hasTitleSatisfying(title ->
                                       title.hasRaw(TITLE + " UPDATED")
                                            .hasRendered(TITLE + " UPDATED"))
                               .hasContentSatisfying(content ->
                                       content.hasRaw(CONTENT + " UPDATED")
                                              .hasRendered(toBlock(CONTENT + " UPDATED")))
                               .hasExcerptSatisfying(excerpt ->
                                       excerpt.hasRaw(CONTENT + " UPDATED")
                                              .hasRendered(toBlock(CONTENT + " UPDATED")));
        }
    }

    @DisplayName("'POST TYPE' Operations")
    @Nested
    class PostTypeTests {

        @DisplayName("'GET' succeeds")
        @Test
        void getFailsOnBlankOrNullParameter() {

            assertThatThrownBy(() -> client.postTypes().get(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("name is marked non-null but is null");

            assertThatThrownBy(() -> client.postTypes().get(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name cannot be blank");

            assertThatThrownBy(() -> client.postTypes().get("    "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name cannot be blank");
        }

        @DisplayName("'GET' succeeds")
        @Test
        void getSucceeds() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post-types/get.success.json");

            // WHEN
            final WpPostType postType = client.postTypes().get("wp_navigation");

            // THEN
            assertThat(postType)
                    .isNotNull();
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post-types/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.postTypes().list());
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post-types/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.postTypes().list());
        }

        @DisplayName("'LIST' succeeds")
        @Test
        void listSucceeds() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post-types/list.success.json");

            // WHEN
            final Map<String, WpPostType> postTypes = client.postTypes().list();

            // THEN
            assertThat(postTypes)
                    .containsOnlyKeys(
                            "post",
                            "page",
                            "attachment",
                            "nav_menu_item",
                            "wp_block",
                            "wp_template",
                            "wp_template_part",
                            "wp_global_styles",
                            "wp_navigation",
                            "wp_font_family",
                            "wp_font_face");

            final List<String> names = postTypes.keySet().stream().map(key -> postTypes.get(key).getName()).toList();
            final List<String> restBases = postTypes.keySet().stream().map(key -> postTypes.get(key).getRestBase()).toList();

            assertThat(names)
                    .containsOnly(
                            "Posts",
                            "Pages",
                            "Media",
                            "Navigation Menu Items",
                            "Patterns",
                            "Templates",
                            "Template Parts",
                            "Global Styles",
                            "Navigation Menus",
                            "Font Families",
                            "Font Faces");

            assertThat(restBases)
                    .containsOnly(
                            "posts",
                            "pages",
                            "media",
                            "menu-items",
                            "blocks",
                            "templates",
                            "template-parts",
                            "global-styles",
                            "navigation",
                            "font-families",
                            "font-families/(?P<font_family_id>[\\d]+)/font-faces");
        }
    }

    @DisplayName("'STATUS' Operations")
    @Nested
    class StatusTests {

        @DisplayName("'GET' succeeds")
        @Test
        void getFailsOnBlankOrNullParameter() {

            assertThatThrownBy(() -> client.postStatuses().get(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("name is marked non-null but is null");

            assertThatThrownBy(() -> client.postStatuses().get(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name cannot be blank");

            assertThatThrownBy(() -> client.postStatuses().get("    "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name cannot be blank");
        }

        @DisplayName("'GET' succeeds")
        @Test
        void getSucceeds() {

            // GIVEN
            givenExpectationFromFile("basic-auth/statuses/get.success.json");

            // WHEN
            final WpStatus status = client.postStatuses().get("publish");

            // THEN
            assertThat(status)
                    .isNotNull();
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/statuses/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.postStatuses().list());
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/statuses/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.postStatuses().list());
        }

        @DisplayName("'LIST' succeeds")
        @Test
        void listSucceeds() {

            // GIVEN
            givenExpectationFromFile("basic-auth/statuses/list.success.json");

            // WHEN
            final Map<String, WpStatus> statuses = client.postStatuses().list();

            // THEN
            assertThat(statuses)
                    .containsOnlyKeys("publish", "future", "draft", "pending", "private", "trash");
        }
    }

    @DisplayName("'TAG' Operations")
    @Nested
    class TagTests {

        public static final String TAG_1_DESCRIPTION = "Tag #1";
        public static final String TAG_1_NAME = "tag1";
        public static final String TAG_1_SLUG = "tag-1";

        public static final String TAG_2_NAME = "tag2";
        public static final String TAG_2_DESCRIPTION = "Tag #2";
        public static final String TAG_2_SLUG = "tag-2";

        public static final String MY_TAG_NAME = "my tag";
        public static final String MY_TAG_DESCRIPTION = "my description";
        public static final String MY_TAG_SLUG = "my-tag";

        @DisplayName("'CREATE' fails on HTTP BAD REQUEST")
        @Test
        void createFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/create.failure.bad-request.json");

            final String NAME = MY_TAG_NAME;
            final String DESCRIPTION = MY_TAG_DESCRIPTION;
            final String SLUG = MY_TAG_SLUG;

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
                                            .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.tags().create(createRequest));
        }

        @DisplayName("'CREATE' fails on blank name")
        @Test
        void createFailsOnBlankName() {

            // GIVEN
            final String NAME = "  ";

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(MY_TAG_DESCRIPTION)
                                            .withSlug(MY_TAG_SLUG)
                                            .build();

            // WHEN/THEN
            assertThatThrownBy(() -> client.tags().create(createRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name cannot be blank");
        }

        @DisplayName("'CREATE' fails on HTTP FORBIDDEN")
        @Test
        void createFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/create.failure.forbidden.json");

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(MY_TAG_NAME)
                                            .withDescription(MY_TAG_DESCRIPTION)
                                            .withSlug(MY_TAG_SLUG)
                                            .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.tags().create(createRequest));
        }

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.tags().create(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");
        }

        @DisplayName("'CREATE' fails on HTTP UNAUTHORIZED")
        @Test
        void createFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/create.failure.unauthorized.json");

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(MY_TAG_NAME)
                                            .withDescription(MY_TAG_DESCRIPTION)
                                            .withSlug(MY_TAG_SLUG)
                                            .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.tags().create(createRequest));
        }

        @DisplayName("'CREATE' works")
        @Test
        void createWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/create.success.json");

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(MY_TAG_NAME)
                                            .withDescription(MY_TAG_DESCRIPTION)
                                            .withSlug(MY_TAG_SLUG)
                                            .build();

            // WHEN
            final WpTag tag = client.tags().create(createRequest);

            // THEN
            WordPressAssertions.assertThat(tag)
                               .isNotNull()
                               .hasCount(0)
                               .hasDescription(MY_TAG_DESCRIPTION)
                               .hasName(MY_TAG_NAME)
                               .hasSlug(MY_TAG_SLUG)
                               .hasTaxonomy(POST_TAG);
        }

        @DisplayName("'DELETE' fails on HTTP FORBIDDEN")
        @Test
        void deleteFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/delete.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.tags().delete(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.tags().delete(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.tags().delete(1005L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/delete.success.json");

            // WHEN
            final WpTagDeletionResponse response = client.tags().delete(1005L);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(1005L)
                                              .hasCount(0)
                                              .hasDescription(TAG_1_DESCRIPTION)
                                              .hasName(TAG_1_NAME)
                                              .hasSlug(TAG_1_SLUG)
                                              .hasTaxonomy(POST_TAG)
                               );
        }

        @DisplayName("'GET' fails on HTTP FORBIDDEN")
        @Test
        void getFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.tags().get(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.tags().get(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.tags().get(1005L, null));
        }

        @DisplayName("'GET' works (with context)")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.success.with-context.json");

            final long tagId = 4L;

            // WHEN
            final WpTag tag = client.tags().get(tagId, EDIT);

            // THEN
            WordPressAssertions.assertThat(tag)
                               .isNotNull()
                               .hasId(tagId)
                               .hasCount(0)
                               .hasDescription(TAG_1_DESCRIPTION)
                               .hasName(TAG_1_NAME)
                               .hasSlug(TAG_1_SLUG)
                               .hasTaxonomy(POST_TAG);
        }

        @DisplayName("'GET' works (with no context - default)")
        @Test
        void getWorksWithNoContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.success.without-context.json");

            final long tagId = 4L;

            // WHEN
            final WpTag tag = client.tags().get(tagId, null);

            // THEN
            WordPressAssertions.assertThat(tag)
                               .isNotNull()
                               .hasId(tagId)
                               .hasCount(0)
                               .hasDescription(TAG_1_DESCRIPTION)
                               .hasName(TAG_1_NAME)
                               .hasSlug(TAG_1_SLUG)
                               .hasTaxonomy(POST_TAG);
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.tags().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.tags().list(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("paginationQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.tags().list(new WpPaginationQuery(1, 10), null));
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void listWorksWithJustPaging() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/list.success.just-paging.json");

            // WHEN
            final WpPagedResponse<WpTag> response = client.tags().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       assertThat(r.getItems())
                                               .hasSize(2)
                                               .satisfiesExactly(
                                                       tag ->
                                                               WordPressAssertions.assertThat(tag)
                                                                                  .isNotNull()
                                                                                  .hasName(TAG_1_NAME)
                                                                                  .hasDescription(TAG_1_DESCRIPTION)
                                                                                  .hasSlug(TAG_1_SLUG)
                                                       ,
                                                       tag ->
                                                               WordPressAssertions.assertThat(tag)
                                                                                  .isNotNull()
                                                                                  .hasName(TAG_2_NAME)
                                                                                  .hasDescription(TAG_2_DESCRIPTION)
                                                                                  .hasSlug(TAG_2_SLUG)
                                               ));
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void listWorksWithPagingAndQuery() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/list.success.paging-and-query.json");

            // WHEN
            final WpTagQuery tagQuery = WpTagQuery.builder()
                                                  .withSlug(TAG_2_SLUG)
                                                  .withExcludeId(2000L)
                                                  .build();

            final WpPagedResponse<WpTag> response = client.tags().list(new WpPaginationQuery(1, 10), tagQuery);

            // THEN

            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage()
                               .satisfies(r ->
                                       assertThat(r.getItems())
                                               .hasSize(1)
                                               .satisfiesExactly(

                                                       tag ->
                                                               WordPressAssertions.assertThat(tag)
                                                                                  .isNotNull()
                                                                                  .hasName(TAG_2_NAME)
                                                                                  .hasDescription(TAG_2_DESCRIPTION)
                                                                                  .hasSlug(TAG_2_SLUG)
                                               ));
        }

        @DisplayName("'UPDATE' fails on HTTP BAD REQUEST")
        @Test
        void updateFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/update.failure.bad-request.json");

            final String NAME = "tag1_updated";
            final String DESCRIPTION = "Tag #1_updated";
            final String SLUG = "tag-1updated";

            final WpTagCreateUpdateRequest updateRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
                                            .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.tags().update(2L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP FORBIDDEN")
        @Test
        void updateFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/update.failure.forbidden.json");

            final String NAME = "tag1_updated";
            final String DESCRIPTION = "Tag #1_updated";
            final String SLUG = "tag-1updated";

            final WpTagCreateUpdateRequest updateRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
                                            .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.tags().update(2L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void updateFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/update.failure.not-found.json");

            final String NAME = "tag1_updated";
            final String DESCRIPTION = "Tag #1_updated";
            final String SLUG = "tag-1updated";

            final WpTagCreateUpdateRequest updateRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
                                            .build();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.tags().update(2L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.tags().update(1000L, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("updateRequest is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on HTTP UNAUTHORIZED")
        @Test
        void updateFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/update.failure.unauthorized.json");

            final String NAME = "tag1_updated";
            final String DESCRIPTION = "Tag #1_updated";
            final String SLUG = "tag-1updated";

            final WpTagCreateUpdateRequest updateRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
                                            .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.tags().update(2L, updateRequest));
        }

        @DisplayName("'UPDATE' works")
        @Test
        void updateWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/update.success.json");

            final String NAME = "tag1_updated";
            final String DESCRIPTION = "Tag #1_updated";
            final String SLUG = "tag-1updated";

            final WpTagCreateUpdateRequest updateRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
                                            .build();

            // WHEN
            final WpTag tag = client.tags().update(2L, updateRequest);

            // THEN
            WordPressAssertions.assertThat(tag)
                               .isNotNull()
                               .hasCount(0)
                               .hasDescription(DESCRIPTION)
                               .hasName(NAME)
                               .hasSlug(SLUG)
                               .hasTaxonomy(POST_TAG);
        }
    }

    @DisplayName("'TAXONOMY' Operations")
    @Nested
    class TaxonomyTests {

        @DisplayName("'GET' succeeds")
        @Test
        void getFailsOnBlankOrNullParameter() {

            assertThatThrownBy(() -> client.taxonomies().get(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("name is marked non-null but is null");

            assertThatThrownBy(() -> client.taxonomies().get(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name cannot be blank");

            assertThatThrownBy(() -> client.taxonomies().get("    "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name cannot be blank");
        }

        @DisplayName("'GET' succeeds")
        @Test
        void getSucceeds() {

            // GIVEN
            givenExpectationFromFile("basic-auth/taxonomies/get.success.json");

            // WHEN
            final WpTaxonomyInfo taxonomyInfo = client.taxonomies().get("post_tag");

            // THEN
            assertThat(taxonomyInfo)
                    .isNotNull();
        }

        @DisplayName("'LIST' fails on HTTP FORBIDDEN")
        @Test
        void listFailsOnForbidden() {

            // GIVEN
            givenExpectationFromFile("basic-auth/taxonomies/list.failure.forbidden.json");

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.taxonomies().list());
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/taxonomies/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.taxonomies().list());
        }

        @DisplayName("'LIST' succeeds")
        @Test
        void listSucceeds() {

            // GIVEN
            givenExpectationFromFile("basic-auth/taxonomies/list.success.json");

            // WHEN
            final Map<String, WpTaxonomyInfo> taxonomies = client.taxonomies().list();

            // THEN
            assertThat(taxonomies)
                    .containsOnlyKeys(
                            "category",
                            "post_tag",
                            "nav_menu",
                            "wp_pattern_category"
                    );

            final List<String> names = taxonomies.keySet().stream().map(key -> taxonomies.get(key).getName()).toList();
            final List<String> restBases = taxonomies.keySet().stream().map(key -> taxonomies.get(key).getRestBase()).toList();

            assertThat(names)
                    .containsOnly(
                            "Categories",
                            "Tags",
                            "Navigation Menus",
                            "Pattern Categories");

            assertThat(restBases)
                    .containsOnly(
                            "categories",
                            "tags",
                            "menus",
                            "wp_pattern_category");
        }
    }
}
