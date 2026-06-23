package io.github.evisentin.wordpress.test.integration.base;

import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.assertions.WordPressAssertions;
import io.github.evisentin.wordpress.rest.client.domain.model.*;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.*;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPageOrderFields;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPostOrderFields;
import io.github.evisentin.wordpress.rest.client.domain.model.query.*;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.*;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.*;
import io.github.evisentin.wordpress.test.integration.BaseWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.WpBasicAuthRestClientFactory;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpOpenClosed.CLOSED;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpOpenClosed.OPEN;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection.ASC;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpTaxonomy.CATEGORY;
import static io.github.evisentin.wordpress.rest.client.domain.model.enums.WpTaxonomy.POST_TAG;
import static io.github.evisentin.wordpress.test.integration.base.SlugUtils.toWordPressSlug;
import static io.github.evisentin.wordpress.test.integration.base.WpAssertions.assertThrowsWpBadRequest;
import static io.github.evisentin.wordpress.test.integration.base.WpAssertions.assertThrowsWpForbidden;
import static io.github.evisentin.wordpress.test.integration.base.WpAssertions.assertThrowsWpNotFound;
import static java.util.Collections.emptySet;

/**
 * Base integration test class for WordPress REST API scenarios using Basic Authentication.
 *
 * <p>This class extends {@link BaseWordPressIntegrationTest} and provides a
 * preconfigured WordPress instance along with ready-to-use REST clients for authenticated testing. It focuses
 * specifically on testing API interactions secured via Basic Authentication.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Ensures WordPress is installed and initialized before tests run</li>
 *   <li>Configures default users and cleans initial data</li>
 *   <li>Initializes REST clients for both admin and standard users</li>
 *   <li>Provides a foundation for API-level integration tests</li>
 * </ul>
 *
 * <h2>Authentication Setup</h2>
 * <p>Two {@link WpRestClient} instances are created:</p>
 * <ul>
 *   <li>{@code adminClient} – authenticated as an administrator using an application password</li>
 *   <li>{@code standardUserClient} – authenticated as a regular user</li>
 * </ul>
 *
 * <h2>Lifecycle</h2>
 * <p>Before all tests, the class ensures that:</p>
 * <ul>
 *   <li>WordPress is installed (if not already)</li>
 *   <li>Default users are created</li>
 *   <li>Permalinks are enabled</li>
 *   <li>Default content is removed</li>
 *   <li>REST clients are initialized via {@link #clientFactory()}</li>
 * </ul>
 *
 * <h2>Extensibility</h2>
 * <p>Concrete subclasses must provide a {@link WpBasicAuthRestClientFactory} implementation
 * by overriding {@link #clientFactory()} to control how REST clients are created.</p>
 *
 * <h2>Typical Usage</h2>
 * <pre>{@code
 * class MyApiTest extends BasicAuthWordPressIntegrationTest {
 *
 *     @Override
 *     protected WpRestClientFactory clientFactory() {
 *         return new MyClientFactory();
 *     }
 *
 *     @Test
 *     void shouldCreateCategory() {
 *         // use adminClient or standardUserClient
 *     }
 * }
 * }</pre>
 *
 * @see BaseWordPressIntegrationTest
 * @see WpRestClient
 * @see WpBasicAuthRestClientFactory
 */
@Slf4j
public abstract class BasicAuthWordPressIntegrationTest extends BaseWordPressIntegrationTest {

    protected WpRestClient adminClient;
    protected WpRestClient standardUserClient;

    @BeforeAll
    void installWordpress() {

        log.info("installWordpress ({}): BEGIN", getWordPressVersion());

        if (!wpIsWordPressInstalled()) {
            log.info("installWordpress ({}): initializing...", getWordPressVersion());
            wpInitWordPress(getHttpsBaseUrl());
            wpConfigureDefaultUsers();
            wpActivatePermalinks();
            wpCleanDefaultData();
            log.info("installWordpress ({}): initialized", getWordPressVersion());
        }

        log.info("installWordpress ({}): END", getWordPressVersion());

        adminClient = clientFactory().create(
                getHttpsBaseUrl(),
                WP_ADMIN_USER_NAME,
                adminApplicationPassword
        );

        standardUserClient = clientFactory().create(
                getHttpsBaseUrl(),
                WP_STANDARD_USER_NAME,
                WP_STANDARD_USER_PASSWORD
        );
    }

    protected abstract WpBasicAuthRestClientFactory clientFactory();

    private Long givenCategoryExists(final String name, final String description, final String slug) {
        return wpCreateCategory(name, description, slug);
    }

    private Long givenCommentExists(final long postId, final String content, final String author, final String authorEmail) {
        return wpCreateComment(postId, content, author, authorEmail);
    }

    private Long givenPageExists(final String title, final String content, final WpPageStatus status) {
        return wpCreatePage(title, content, status);
    }

    private Long givenPageExists(final String title, final String content, final WpPageStatus status, final String password) {
        return wpCreatePage(title, content, status, password);
    }

    private Long givenPostExists(final String title, final String content, final WpPostStatus status) {
        return wpCreatePost(title, content, status);
    }

    private Long givenPostExists(final String title, final String content, final WpPostStatus status, final String password) {
        return wpCreatePost(title, content, status, password);
    }

    private void givenPostIsUpdated(final long id, final String title, final String content) {
        wpUpdatePost(id, title, content);
    }

    private Long givenTagExists(final String name, final String description, final String slug) {
        return wpCreateTag(name, description, slug);
    }

    private static WpCategoryCreateUpdateRequest.WpCategoryCreateUpdateRequestBuilder categoryCreateUpdateRequest() {
        return WpCategoryCreateUpdateRequest.builder();
    }

    private static WpPageCreateUpdateRequest.WpPageCreateUpdateRequestBuilder pageCreateUpdateRequest() {
        return WpPageCreateUpdateRequest.builder();
    }

    private static WpPostCreateUpdateRequest.WpPostCreateUpdateRequestBuilder postCreateUpdateRequest() {
        return WpPostCreateUpdateRequest.builder();
    }

    private static WpTagCreateUpdateRequest.WpTagCreateUpdateRequestBuilder tagCreateUpdateRequest() {
        return WpTagCreateUpdateRequest.builder();
    }

    @DisplayName("Category APIs - Integration Tests")
    @Nested
    class CategoryTests {

        private static final String CATEGORY_1_NAME = "Category #1";
        private static final String CATEGORY_1_DESCRIPTION = "My first category";
        private static final String CATEGORY_1_SLUG = "category-1";

        private static final String CATEGORY_2_NAME = "Category #2";
        private static final String CATEGORY_2_DESCRIPTION = "My second category";
        private static final String CATEGORY_2_SLUG = "category-2";

        @DisplayName("'CREATE' fails on parent not found")
        @Test
        void create__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            Long nonExistingParentId = 1000L;

            // WHEN/THEN

            final WpCategoryCreateUpdateRequest creationRequest =
                    categoryCreateUpdateRequest()
                            .withName(CATEGORY_1_NAME)
                            .withParentId(nonExistingParentId)
                            .build();

            assertThrowsWpBadRequest(() -> adminClient.categories().create(creationRequest),
                    "rest_term_invalid",
                    "Parent term does not exist.");
        }

        @DisplayName("'CREATE' works")
        @Test
        void create__works() {

            // GIVEN
            wpCleanDefaultData();
            Long existingParentCategory = givenCategoryExists("Parent", "Parent Category", "parent");

            // WHEN
            final WpCategory category = adminClient.categories().create(categoryCreateUpdateRequest()
                    .withName(CATEGORY_1_NAME)
                    .withDescription(CATEGORY_1_DESCRIPTION)
                    .withSlug(CATEGORY_1_SLUG)
                    .withParentId(existingParentCategory)
                    .build());
            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasNonZeroId()
                               .hasName(CATEGORY_1_NAME)
                               .hasDescription(CATEGORY_1_DESCRIPTION)
                               .hasSlug(CATEGORY_1_SLUG)
                               .hasParentId(existingParentCategory)
                               .hasCount(0)
                               .hasTaxonomy(CATEGORY)
                               .hasLink(linkForCategory("parent", CATEGORY_1_SLUG));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void delete__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.categories().delete(1000L), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);

            // WHEN
            final WpCategoryDeletionResponse deletionResponse = adminClient.categories().delete(categoryId);

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(categoryId)
                                              .hasCount(0)
                                              .hasDescription(CATEGORY_1_DESCRIPTION)
                                              .hasName(CATEGORY_1_NAME)
                                              .hasSlug(CATEGORY_1_SLUG)
                                              .hasLink(linkForCategory(CATEGORY_1_SLUG))
                                              .hasTaxonomy(CATEGORY));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void get__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.categories().get(categoryId + 1000, null), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'GET' works with context")
        @Test
        void get__works_with_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);

            // WHEN
            final WpCategory category = adminClient.categories().get(categoryId, WpContext.EDIT);

            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(categoryId)
                               .hasCount(0)
                               .hasDescription(CATEGORY_1_DESCRIPTION)
                               .hasName(CATEGORY_1_NAME)
                               .hasSlug(CATEGORY_1_SLUG)
                               .hasTaxonomy(CATEGORY);
        }

        @DisplayName("'GET' works with no context")
        @Test
        void get__works_with_no_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);

            // WHEN
            final WpCategory category = adminClient.categories().get(categoryId, null);

            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(categoryId)
                               .hasCount(0)
                               .hasDescription(CATEGORY_1_DESCRIPTION)
                               .hasName(CATEGORY_1_NAME)
                               .hasSlug(CATEGORY_1_SLUG)
                               .hasTaxonomy(CATEGORY);
        }

        @DisplayName("'LIST' works with just paging")
        @Test
        void list__works_with_just_paging() {

            // GIVEN
            wpCleanDefaultData();

            final Long cat1_id = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);
            final Long cat2_id = givenCategoryExists(CATEGORY_2_NAME, CATEGORY_2_DESCRIPTION, CATEGORY_2_SLUG);

            // WHEN
            final WpPagedResponse<WpCategory> response = adminClient.categories().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(3)
                               .doesNotHaveNextPage();

            assertThat(response.getItems())
                    .isNotNull()
                    .hasSize(3)
                    .extracting(
                            WpCategory::getId,
                            WpCategory::getName,
                            WpCategory::getDescription,
                            WpCategory::getSlug,
                            WpCategory::getTaxonomy
                    )
                    .containsExactly(
                            tuple(cat1_id, CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG, CATEGORY),
                            tuple(cat2_id, CATEGORY_2_NAME, CATEGORY_2_DESCRIPTION, CATEGORY_2_SLUG, CATEGORY),
                            tuple(1L, "Uncategorized", "", "uncategorized", CATEGORY)
                    );
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void list__works_with_paging_and_query() {

            // GIVEN
            wpCleanDefaultData();

            final Long cat1_id = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);
            final Long cat2_id = givenCategoryExists(CATEGORY_2_NAME, CATEGORY_2_DESCRIPTION, CATEGORY_2_SLUG);

            // Filter just for category #2
            final WpCategoryQuery categoryQuery = WpCategoryQuery.builder()
                                                                 .withSlug(CATEGORY_2_SLUG)
                                                                 .build();

            // WHEN
            final WpPagedResponse<WpCategory> response = adminClient.categories().list(new WpPaginationQuery(1, 10), categoryQuery);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage();

            assertThat(response.getItems())
                    .isNotNull()
                    .hasSize(1)
                    .extracting(
                            WpCategory::getId,
                            WpCategory::getName,
                            WpCategory::getDescription,
                            WpCategory::getSlug,
                            WpCategory::getTaxonomy)
                    .containsExactly(
                            tuple(cat2_id, CATEGORY_2_NAME, CATEGORY_2_DESCRIPTION, CATEGORY_2_SLUG, CATEGORY)
                    );
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void update__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();
            long nonExistingCategoryId = 1000L;

            // WHEN/THEN
            final WpCategoryCreateUpdateRequest updateRequest = categoryCreateUpdateRequest().build();
            assertThrowsWpNotFound(() -> adminClient.categories().update(nonExistingCategoryId, updateRequest), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'UPDATE' fails on parent not found")
        @Test
        void update__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            Long nonExistingCategoryId = 1000L;
            final Long categoryId = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);

            // WHEN/THEN
            final WpCategoryCreateUpdateRequest updateRequest = categoryCreateUpdateRequest()
                    .withName("my category")
                    .withParentId(nonExistingCategoryId)
                    .build();

            assertThrowsWpBadRequest(() -> adminClient.categories().update(categoryId, updateRequest),
                    "rest_term_invalid", "Parent term does not exist.");
        }

        @DisplayName("'UPDATE' works")
        @Test
        void update__works() {

            // GIVEN
            wpCleanDefaultData();

            final Long categoryId = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);

            // WHEN
            final WpCategoryCreateUpdateRequest updateRequest =
                    categoryCreateUpdateRequest()
                            .withName(CATEGORY_1_NAME + " UPDATED")
                            .withDescription(CATEGORY_1_DESCRIPTION + " UPDATED")
                            .withSlug(CATEGORY_1_SLUG + " UPDATED")
                            .build();

            final WpCategory category = adminClient.categories().update(categoryId, updateRequest);

            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(categoryId)
                               .hasCount(0)
                               .hasDescription(CATEGORY_1_DESCRIPTION + " UPDATED")
                               .hasName(CATEGORY_1_NAME + " UPDATED")
                               .hasSlug(toWordPressSlug(CATEGORY_1_SLUG + " UPDATED"))
                               .hasTaxonomy(CATEGORY);
        }

        private String linkForCategory(final @NonNull String slug) {
            return String.format("%s/category/%s/", getHttpsBaseUrl(), slug);
        }

        private String linkForCategory(final @NonNull String parentSlug, final @NonNull String slug) {
            return String.format("%s/category/%s/%s/", getHttpsBaseUrl(), parentSlug, slug);
        }
    }

    @DisplayName("Media APIs - Integration Tests")
    @Nested
    class MediaTests {

        @DisplayName("'CREATE' works")
        @Test
        @SneakyThrows
        void create__works() {

            // GIVEN
            wpCleanDefaultData();
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("files/sample.png");

            Path tempFile = Files.createTempFile("sample", ".png");

            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            File file = tempFile.toFile();

            // WHEN
            final WpMedia media = adminClient.media().create(file, "sample.png", "image/png");
            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull()
                               .hasNonZeroId()
                               .hasSlugStartingWith("sample")
                               .hasType("attachment")
                               .hasAuthorId(1L)
                               .hasCommentStatus(WpOpenClosed.OPEN)
                               .hasPingStatus(WpOpenClosed.CLOSED)
                               .hasMediaType(WpMediaType.IMAGE)
                               .hasMimeType("image/png");
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void delete__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.media().delete(1000L), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'DELETE' works")
        @Test
        @SneakyThrows
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("files/sample.png");

            Path tempFile = Files.createTempFile("sample", ".png");

            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            File file = tempFile.toFile();
            final WpMedia existingMedia = adminClient.media().create(file, "sample.png", "image/png");

            // WHEN
            final WpMediaDeletionResponse deletionResponse = adminClient.media().delete(existingMedia.getId());

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(existingMedia.getId())
                               );
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void get__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.media().get(10L, null), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'GET' works with context")
        @Test
        @SneakyThrows
        void get__works_with_context() {

            // GIVEN
            wpCleanDefaultData();
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("files/sample.png");

            Path tempFile = Files.createTempFile("sample", ".png");

            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            File file = tempFile.toFile();
            final WpMedia existingMedia = adminClient.media().create(file, "sample.png", "image/png");

            // WHEN
            final WpMedia media = adminClient.media().get(existingMedia.getId(), WpContext.EDIT);

            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull();

            assertThat(media).usingRecursiveComparison()
                             .isEqualTo(existingMedia);
        }

        @DisplayName("'GET' works with no context")
        @Test
        @SneakyThrows
        void get__works_with_no_context() {

            // GIVEN
            wpCleanDefaultData();
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("files/sample.png");

            Path tempFile = Files.createTempFile("sample", ".png");

            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            File file = tempFile.toFile();
            final WpMedia existingMedia = adminClient.media().create(file, "sample.png", "image/png");

            // WHEN
            final WpMedia media = adminClient.media().get(existingMedia.getId(), null);

            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull();

            // with context VIEW ( default if null ) some properties are set to null, we must adjust before comparison
            existingMedia.getCaption().setRaw(null);
            existingMedia.getDescription().setRaw(null);
            existingMedia.getGuid().setRaw(null);
            existingMedia.setGeneratedSlug(null);
            existingMedia.setPermalinkTemplate(null);
            existingMedia.getTitle().setRaw(null);

            assertThat(media).usingRecursiveComparison()
                             .isEqualTo(existingMedia);
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void update__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();
            long nonExistingMediaId = 9L;

            // WHEN/THEN
            final WpMediaUpdateRequest updateRequest = WpMediaUpdateRequest.builder().build();
            assertThrowsWpNotFound(() -> adminClient.media().update(nonExistingMediaId, updateRequest), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'UPDATE' works")
        @Test
        @SneakyThrows
        void update__works() {

            // GIVEN
            wpCleanDefaultData();

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("files/sample.png");

            Path tempFile = Files.createTempFile("sample", ".png");

            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            File file = tempFile.toFile();
            final WpMedia existingMedia = adminClient.media().create(file, "sample.png", "image/png");

            // WHEN
            final WpMediaUpdateRequest updateRequest =
                    WpMediaUpdateRequest.builder()
                                        .withTitle("Title updated")
                                        .withDescription("Description updated")
                                        .withSlug("Slug updated")
                                        .build();

            final WpMedia media = adminClient.media().update(existingMedia.getId(), updateRequest);

            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull()
                               .hasId(existingMedia.getId())
                               .hasDescriptionSatisfying(t -> t.hasRaw("Description updated"))
                               .hasTitleSatisfying(t -> t.hasRaw("Title updated"))
                               .hasSlug("slug-updated");
        }
    }

    @DisplayName("Post Revision APIs - Integration Tests")
    @Nested
    class PostRevisionTests {

        @DisplayName("'GET' fails on post not found")
        @Test
        void get__fails_on_post_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.postRevisions().get(1000L, 1L), "rest_post_invalid_parent", "Invalid post parent ID.");
        }

        @DisplayName("'GET' fails on post revision not found")
        @Test
        void get__fails_on_post_revision_not_found() {
            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists("My post", "My content", WpPostStatus.PUBLISH);

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.postRevisions().get(existingPostId, 1L), "rest_post_invalid_id", "Invalid revision ID.");
        }

        @DisplayName("'GET' succeeds")
        @Test
        void get__succeeds() {
            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists("My post", "My content", WpPostStatus.PUBLISH);
            givenPostIsUpdated(existingPostId, "My post #1", "My content");
            final List<Long> revisionIds = wpGetRevisionIds(existingPostId);

            // WHEN/THEN
            final WpPostRevision postRevision = adminClient.postRevisions().get(existingPostId, revisionIds.getFirst());

            assertThat(postRevision).isNotNull();
        }

        @DisplayName("'LIST' returns empty on no revision")
        @Test
        void list__empty_on_no_revisions() {
            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists("My post", "My content", WpPostStatus.PUBLISH);

            // WHEN/THEN
            final WpPagedResponse<WpPostRevision> response = adminClient.postRevisions().list(existingPostId, new WpPaginationQuery(1, 10), null);

            WordPressAssertions.assertThat(response)
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(0)
                               .hasTotalItems(0)
                               .doesNotHaveNextPage();
        }

        @DisplayName("'LIST' fails on post not found")
        @Test
        void list__fails_on_post_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.postRevisions().list(1000L, new WpPaginationQuery(1, 10), null), "rest_post_invalid_parent", "Invalid post parent ID.");
        }

        @DisplayName("'LIST' returns revisions")
        @Test
        void list__returns_revisions() {
            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists("My post", "My content", WpPostStatus.PUBLISH);
            givenPostIsUpdated(existingPostId, "My post #1", "My content"); // first update -> first revision
            givenPostIsUpdated(existingPostId, "My post #1", "My content #1"); // second update -> second revision

            // WHEN/THEN
            final WpPagedResponse<WpPostRevision> response = adminClient.postRevisions().list(existingPostId, new WpPaginationQuery(1, 10), null);

            WordPressAssertions.assertThat(response)
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)
                               .doesNotHaveNextPage();
        }
    }

    @DisplayName("Page APIs - Integration Tests")
    @Nested
    class PageTests {

        private static final String PAGE_1_TITLE = "Page #1";
        private static final String PAGE_1_CONTENT = "My first page";
        private static final String PAGE_1_SLUG = "page-1";

        private static final String PAGE_2_TITLE = "Page #2";

        private static final String PAGE_3_TITLE = "Page #3";

        @DisplayName("'CREATE' works")
        @Test
        void create__works() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            WpPageCreateUpdateRequest createUpdateRequest =
                    pageCreateUpdateRequest()
                            .withTitle(PAGE_1_TITLE)
                            .withContent(PAGE_1_CONTENT)
                            .build();

            final WpPage page = adminClient.pages().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId()
                               .hasLink(linkForPage(page.getId()))
                               .hasSlug("")
                               .hasGeneratedSlug(toWordPressSlug(PAGE_1_TITLE))
                               .hasStatus(WpPageStatus.DRAFT)
                               .hasCommentStatus(CLOSED)
                               .hasPingStatus(CLOSED)
                               .hasType("page")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(PAGE_1_TITLE)
                                        .hasRendered(PAGE_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(PAGE_1_CONTENT)
                                        .hasRendered(toBlock(PAGE_1_CONTENT))
                                        .isNotProtected()
                                        .hasBlockVersion(0))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw("")
                                        .hasRendered(toBlock(PAGE_1_CONTENT))
                                        .isNotProtected()
                                        .hasBlockVersion(null));
        }

        @DisplayName("'CREATE' works with password")
        @Test
        void create__works_with_password() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            WpPageCreateUpdateRequest createUpdateRequest =
                    pageCreateUpdateRequest()
                            .withTitle(PAGE_1_TITLE)
                            .withContent(PAGE_1_CONTENT)
                            .withPassword("my password")
                            .withStatus(WpPageStatus.PUBLISH)
                            .build();

            final WpPage page = adminClient.pages().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId()
                               .hasLink(linkForPage(page.getSlug())) // not draft, the link has the slug
                               .hasSlug(toWordPressSlug(PAGE_1_TITLE))
                               .hasGeneratedSlug(toWordPressSlug(PAGE_1_TITLE))
                               .hasStatus(WpPageStatus.PUBLISH)
                               .hasPassword("my password")
                               .hasCommentStatus(CLOSED)
                               .hasPingStatus(CLOSED)
                               .hasType("page")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(PAGE_1_TITLE)
                                        .hasRendered(PAGE_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(PAGE_1_CONTENT)
                                        .hasRendered(toBlock(PAGE_1_CONTENT))
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(0))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw("")
                                        .hasRendered(toBlock(PAGE_1_CONTENT))
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void delete__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.pages().delete(1000L), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPageId = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH);

            // WHEN
            final WpPageDeletionResponse deletionResponse = adminClient.pages().delete(existingPageId);

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(existingPageId)
                                              .hasTitleSatisfying(t ->
                                                      t.hasRaw(PAGE_1_TITLE)
                                                       .hasRendered(PAGE_1_TITLE))
                                              .hasContentSatisfying(c ->
                                                      c.hasRaw(PAGE_1_CONTENT)
                                                       .hasRendered(toBlock(PAGE_1_CONTENT))
                                                       .isNotProtected()
                                                       .hasBlockVersion(0))
                                              .hasExcerptSatisfying(e ->
                                                      e.hasRaw("")
                                                       .hasRendered(toBlock(PAGE_1_CONTENT))
                                                       .isNotProtected()
                                                       .hasBlockVersion(null))
                                              .hasSlug(PAGE_1_SLUG)
                                              .hasStatus(WpPageStatus.PUBLISH));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void get__fails_on_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.pages().get(1000L, null), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'GET' works with password")
        @Test
        void get__gets_content_on_password_protected_post_and_password() {

            // GIVEN
            wpCleanDefaultData();

            final String PASSWORD = "password$$";

            final Long existingPageId = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH, PASSWORD);

            // WHEN
            final WpPage page = standardUserClient.pages().get(existingPageId, WpContext.VIEW, PASSWORD);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId()
                               .hasStatus(WpPageStatus.PUBLISH)
                               .hasPassword(null)
                               .hasCommentStatus(CLOSED)
                               .hasPingStatus(CLOSED)
                               .hasType("page")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(null)
                                        .hasRendered(PAGE_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(null)
                                        .hasRendered(toBlock(PAGE_1_CONTENT))
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw(null)
                                        .hasRendered(toBlock(PAGE_1_CONTENT))
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null));
        }

        @DisplayName("'GET' returns empty content if protected and no password provided")
        @Test
        void get__gets_empty_content_on_password_protected_post_and_no_password() {

            // GIVEN
            wpCleanDefaultData();
            final String PASSWORD = "password$$";

            final Long existingPageId = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH, PASSWORD);

            // WHEN
            final WpPage page = standardUserClient.pages().get(existingPageId, WpContext.VIEW);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId()
                               .hasStatus(WpPageStatus.PUBLISH)
                               .hasPassword(null)
                               .hasCommentStatus(CLOSED)
                               .hasPingStatus(CLOSED)
                               .hasType("page")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(null)
                                        .hasRendered(PAGE_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(null)
                                        .hasRendered("")
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw(null)
                                        .hasRendered("")
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null));
        }

        @DisplayName("'GET' works with context")
        @Test
        void get__works_with_context() {

            // GIVEN
            wpCleanDefaultData();

            final Long existingPageId = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH);

            // WHEN
            final WpPage page = adminClient.pages().get(existingPageId, WpContext.EDIT);

            // THEN
            assertThat(page).isNotNull();
            assertThat(page.getId()).isNotNull().isEqualTo(existingPageId);
        }

        @DisplayName("'GET' works without context")
        @Test
        void get__works_with_no_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPageId = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH);

            // WHEN
            final WpPage page = adminClient.pages().get(existingPageId, null);

            // THEN
            assertThat(page).isNotNull();
            assertThat(page.getId()).isNotNull().isEqualTo(existingPageId);
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void list__works_with_just_paging() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPageId = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH);

            // WHEN
            final WpPagedResponse<WpPage> response = adminClient.pages().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage();
            assertThat(response.getItems())
                    .isNotNull()
                    .hasSize(1)
                    .first()
                    .satisfies(item ->
                            WordPressAssertions.assertThat(item)
                                               .hasId(existingPageId)
                                               .hasStatus(WpPageStatus.PUBLISH)
                                               // during listing we have just "rendered", not "raw"
                                               .hasTitleSatisfying(title -> title.hasRendered(PAGE_1_TITLE))
                                               // during listing we have just "rendered", not "raw"
                                               .hasContentSatisfying(content -> content.hasRendered(toBlock(PAGE_1_CONTENT)))
                                               // during listing we have just "rendered", not "raw"
                                               .hasExcerptSatisfying(excerpt -> excerpt.hasRendered(toBlock(PAGE_1_CONTENT)))
                    );
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void list__works_with_paging_and_query() {

            // GIVEN
            wpCleanDefaultData();

            final Long draft_page_id = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.DRAFT);
            final Long private_page_id = givenPageExists(PAGE_2_TITLE, PAGE_1_CONTENT, WpPageStatus.PRIVATE);
            final Long published_page_id = givenPageExists(PAGE_3_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH);

            // WHEN
            final WpPageQuery query = WpPageQuery.builder()
                                                 .withStatus(WpPageStatus.DRAFT)
                                                 .withStatus(WpPageStatus.PRIVATE)
                                                 .withOrder(ASC)
                                                 .withOrderBy(WpPageOrderFields.ID)
                                                 .build();

            final WpPagedResponse<WpPage> response = adminClient.pages().list(new WpPaginationQuery(1, 10), query);

            // THEN
            WordPressAssertions.assertThat(response)
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)// TWO POSTS (FILTER)
                               .doesNotHaveNextPage();
            assertThat(response.getItems())
                    .isNotNull()
                    .hasSize(2) // TWO POSTS (FILTER)
                    .extracting(WpPage::getId)
                    .containsExactly(draft_page_id, private_page_id);
        }

        @DisplayName("'TRASH' fails on HTTP NOT FOUND")
        @Test
        void trash__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.pages().trash(1000L), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'TRASH' works")
        @Test
        void trash__works() {

            // GIVEN
            wpCleanDefaultData();

            final Long existingPageId = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH);

            // WHEN
            final WpPage page = adminClient.pages().trash(existingPageId);

            // THEN
            WordPressAssertions.assertThat(page)
                               .isNotNull()
                               .hasId(existingPageId)
                               .hasStatus(WpPageStatus.TRASH);
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void update__fails_on_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.pages().update(1000L, pageCreateUpdateRequest().build()),
                    "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'UPDATE' works")
        @Test
        void update__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPageId = givenPageExists(PAGE_1_TITLE, PAGE_1_CONTENT, WpPageStatus.PUBLISH);

            // WHEN
            WpPageCreateUpdateRequest updateRequest =
                    pageCreateUpdateRequest()
                            .withTitle(PAGE_1_TITLE + " UPDATED")
                            .withContent(PAGE_1_CONTENT + " UPDATED")
                            .withExcerpt(PAGE_1_CONTENT + " UPDATED")
                            .withStatus(WpPageStatus.DRAFT)
                            .build();

            final WpPage page = adminClient.pages().update(existingPageId, updateRequest);

            // THEN
            // TODO: fix assertions
            assertThat(page)
                    .isNotNull()
                    .satisfies(p -> {
                        assertThat(p.getId()).isEqualTo(existingPageId);
                        assertThat(p.getStatus()).isEqualTo(WpPageStatus.DRAFT);
                        assertThat(p.getTitle().getRaw()).isNotNull().isEqualTo(PAGE_1_TITLE + " UPDATED");
                        assertThat(p.getContent().getRaw()).isNotNull().isEqualTo(PAGE_1_CONTENT + " UPDATED");
                        assertThat(p.getExcerpt().getRaw()).isNotNull().isEqualTo(PAGE_1_CONTENT + " UPDATED");
                    });
        }

        private String linkForPage(final @NonNull Long id) {
            return String.format("%s/?page_id=%d", getHttpsBaseUrl(), id);
        }

        private String linkForPage(final @NonNull String slug) {
            return String.format("%s/%s/", getHttpsBaseUrl(), slug);
        }
    }

    @DisplayName("Post APIs - Integration Tests")
    @Nested
    class PostTests {

        private static final String POST_1_TITLE = "Post #1";
        private static final String POST_1_CONTENT = "My first post";
        private static final String POST_1_SLUG = "post-1";

        private static final String POST_2_TITLE = "Post #2";

        private static final String POST_3_TITLE = "Post #3";

        @DisplayName("'CREATE' works")
        @Test
        void create__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long tagCH = givenTagExists("switzerland", "Switzerland", "switzerland");
            final Long categoryNews = givenCategoryExists("news", "News", "news");

            // WHEN
            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withCategory(categoryNews)
                            .withTag(tagCH)
                            .build();

            final WpPost post = adminClient.posts().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasLink(linkForPost(post.getId()))
                               .hasSlug("")
                               .hasGeneratedSlug(toWordPressSlug(POST_1_TITLE))
                               .hasStatus(WpPostStatus.DRAFT)
                               .hasCommentStatus(OPEN)
                               .hasPingStatus(OPEN)
                               .hasType("post")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(POST_1_TITLE)
                                        .hasRendered(POST_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(POST_1_CONTENT)
                                        .hasRendered(toBlock(POST_1_CONTENT))
                                        .isNotProtected()
                                        .hasBlockVersion(0))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw("")
                                        .hasRendered(toBlock(POST_1_CONTENT))
                                        .isNotProtected()
                                        .hasBlockVersion(null))
                               .hasCategories(Set.of(categoryNews))
                               .hasTags(Set.of(tagCH));
        }

        @DisplayName("'CREATE' works with non-existing tags or categories")
        @Test
        void create__works_on_non_existing_categories_or_tags() {

            // GIVEN
            wpCleanDefaultData();
            final Long nonExistingTag1 = 1001L, nonExistingTag2 = 1002L, nonExistingTag3 = 1003L;
            final Long nonExistingCategory1 = 2001L, nonExistingCategory2 = 2002L, nonExistingCategory3 = 3003L;

            // WHEN
            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withCategory(nonExistingCategory1)
                            .withCategory(nonExistingCategory2)
                            .withCategory(nonExistingCategory3)
                            .withTag(nonExistingTag1)
                            .withTag(nonExistingTag2)
                            .withTag(nonExistingTag3)
                            .withStatus(WpPostStatus.PUBLISH)
                            .withCommentStatus(WpOpenClosed.CLOSED)
                            .withPingStatus(WpOpenClosed.CLOSED)
                            .build();

            final WpPost post = adminClient.posts().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasLink(linkForPost(post.getSlug())) // not draft, the link has the slug
                               .hasSlug(toWordPressSlug(POST_1_TITLE))
                               .hasGeneratedSlug(toWordPressSlug(POST_1_TITLE))
                               .hasStatus(WpPostStatus.PUBLISH)
                               .hasCommentStatus(CLOSED)
                               .hasPingStatus(CLOSED)
                               .hasType("post")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(POST_1_TITLE)
                                        .hasRendered(POST_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(POST_1_CONTENT)
                                        .hasRendered(toBlock(POST_1_CONTENT))
                                        .isNotProtected()
                                        .hasBlockVersion(0))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw("")
                                        .hasRendered(toBlock(POST_1_CONTENT))
                                        .isNotProtected()
                                        .hasBlockVersion(null))
                               .hasCategories(emptySet())
                               .hasTags(emptySet());
        }

        @DisplayName("'CREATE' works with password")
        @Test
        void create__works_with_password() {

            // GIVEN
            wpCleanDefaultData();
            final Long tagCH = givenTagExists("switzerland", "Switzerland", "switzerland");
            final Long categoryNews = givenCategoryExists("news", "News", "news");

            // WHEN
            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withCategory(categoryNews)
                            .withTag(tagCH)
                            .withPassword("my password")
                            .withStatus(WpPostStatus.PUBLISH)
                            .build();

            final WpPost post = adminClient.posts().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasLink(linkForPost(post.getSlug())) // not draft, the link has the slug
                               .hasSlug(toWordPressSlug(POST_1_TITLE))
                               .hasGeneratedSlug(toWordPressSlug(POST_1_TITLE))
                               .hasStatus(WpPostStatus.PUBLISH)
                               .hasPassword("my password")
                               .hasCommentStatus(OPEN)
                               .hasPingStatus(OPEN)
                               .hasType("post")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(POST_1_TITLE)
                                        .hasRendered(POST_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(POST_1_CONTENT)
                                        .hasRendered(toBlock(POST_1_CONTENT))
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(0))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw("")
                                        .hasRendered(toBlock(POST_1_CONTENT))
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null))
                               .hasCategories(Set.of(categoryNews))
                               .hasTags(Set.of(tagCH));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void delete__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.posts().delete(1000L), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH);

            // WHEN
            final WpPostDeletionResponse deletionResponse = adminClient.posts().delete(existingPostId);

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(existingPostId)
                                              .hasTitleSatisfying(t ->
                                                      t.hasRaw(POST_1_TITLE)
                                                       .hasRendered(POST_1_TITLE))
                                              .hasContentSatisfying(c ->
                                                      c.hasRaw(POST_1_CONTENT)
                                                       .hasRendered(toBlock(POST_1_CONTENT))
                                                       .isNotProtected()
                                                       .hasBlockVersion(0))
                                              .hasExcerptSatisfying(e ->
                                                      e.hasRaw("")
                                                       .hasRendered(toBlock(POST_1_CONTENT))
                                                       .isNotProtected()
                                                       .hasBlockVersion(null))
                                              .hasSlug(POST_1_SLUG)
                                              .hasStatus(WpPostStatus.PUBLISH));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void get__fails_on_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.posts().get(1000L, null), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'GET' works with password")
        @Test
        void get__gets_content_on_password_protected_post_and_password() {

            // GIVEN
            wpCleanDefaultData();

            final String PASSWORD = "password$$";

            final Long existingPostId = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH, PASSWORD);

            // WHEN
            final WpPost post = standardUserClient.posts().get(existingPostId, WpContext.VIEW, PASSWORD);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasStatus(WpPostStatus.PUBLISH)
                               .hasPassword(null)
                               .hasCommentStatus(OPEN)
                               .hasPingStatus(OPEN)
                               .hasType("post")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(null)
                                        .hasRendered(POST_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(null)
                                        .hasRendered(toBlock(POST_1_CONTENT))
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw(null)
                                        .hasRendered(toBlock(POST_1_CONTENT))
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null));
        }

        @DisplayName("'GET' returns empty content if protected and no password provided")
        @Test
        void get__gets_empty_content_on_password_protected_post_and_no_password() {

            // GIVEN
            wpCleanDefaultData();
            final String PASSWORD = "password$$";

            final Long existingPostId = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH, PASSWORD);

            // WHEN
            final WpPost post = standardUserClient.posts().get(existingPostId, WpContext.VIEW);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasStatus(WpPostStatus.PUBLISH)
                               .hasPassword(null)
                               .hasCommentStatus(OPEN)
                               .hasPingStatus(OPEN)
                               .hasType("post")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw(null)
                                        .hasRendered(POST_1_TITLE))
                               .hasContentSatisfying(c ->
                                       c.hasRaw(null)
                                        .hasRendered("")
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null))
                               .hasExcerptSatisfying(e ->
                                       e.hasRaw(null)
                                        .hasRendered("")
                                        .isProtected() // PASSWORD --> PROTECTED
                                        .hasBlockVersion(null));
        }

        @DisplayName("'GET' works with context")
        @Test
        void get__works_with_context() {

            // GIVEN
            wpCleanDefaultData();

            final Long existingPostId = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH);

            // WHEN
            final WpPost post = adminClient.posts().get(existingPostId, WpContext.EDIT);

            // THEN
            assertThat(post).isNotNull();
            assertThat(post.getId()).isNotNull().isEqualTo(existingPostId);
        }

        @DisplayName("'GET' works without context")
        @Test
        void get__works_with_no_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH);

            // WHEN
            final WpPost post = adminClient.posts().get(existingPostId, null);

            // THEN
            assertThat(post).isNotNull();
            assertThat(post.getId()).isNotNull().isEqualTo(existingPostId);
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void list__works_with_just_paging() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH);

            // WHEN
            final WpPagedResponse<WpPost> response = adminClient.posts().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage();
            assertThat(response.getItems())
                    .isNotNull()
                    .hasSize(1)
                    .first()
                    .satisfies(item ->
                            WordPressAssertions.assertThat(item)
                                               .hasId(existingPostId)
                                               .hasStatus(WpPostStatus.PUBLISH)
                                               // during listing we have just "rendered", not "raw"
                                               .hasTitleSatisfying(title -> title.hasRendered(POST_1_TITLE))
                                               // during listing we have just "rendered", not "raw"
                                               .hasContentSatisfying(content -> content.hasRendered(toBlock(POST_1_CONTENT)))
                                               // during listing we have just "rendered", not "raw"
                                               .hasExcerptSatisfying(excerpt -> excerpt.hasRendered(toBlock(POST_1_CONTENT)))
                    );
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void list__works_with_paging_and_query() {

            // GIVEN
            wpCleanDefaultData();

            final Long draft_post_id = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.DRAFT);
            final Long private_post_id = givenPostExists(POST_2_TITLE, POST_1_CONTENT, WpPostStatus.PRIVATE);
            final Long published_post_id = givenPostExists(POST_3_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH);

            // WHEN
            final WpPostQuery postQuery = WpPostQuery.builder()
                                                     .withStatus(WpPostStatus.DRAFT)
                                                     .withStatus(WpPostStatus.PRIVATE)
                                                     .withOrder(ASC)
                                                     .withOrderBy(WpPostOrderFields.ID)
                                                     .build();

            final WpPagedResponse<WpPost> response = adminClient.posts().list(new WpPaginationQuery(1, 10), postQuery);

            // THEN
            WordPressAssertions.assertThat(response)
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)// TWO POSTS (FILTER)
                               .doesNotHaveNextPage();
            assertThat(response.getItems())
                    .isNotNull()
                    .hasSize(2) // TWO POSTS (FILTER)
                    .extracting(WpPost::getId)
                    .containsExactly(draft_post_id, private_post_id);
        }

        @DisplayName("'TRASH' fails on HTTP NOT FOUND")
        @Test
        void trash__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.posts().trash(1000L), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'TRASH' works")
        @Test
        void trash__works() {

            // GIVEN
            wpCleanDefaultData();

            final Long existingPostId = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH);

            // WHEN
            final WpPost post = adminClient.posts().trash(existingPostId);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId(existingPostId)
                               .hasStatus(WpPostStatus.TRASH);
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void update__fails_on_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.posts().update(1000L, postCreateUpdateRequest().build()),
                    "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'UPDATE' works")
        @Test
        void update__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists(POST_1_TITLE, POST_1_CONTENT, WpPostStatus.PUBLISH);

            // WHEN
            WpPostCreateUpdateRequest updateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE + " UPDATED")
                            .withContent(POST_1_CONTENT + " UPDATED")
                            .withExcerpt(POST_1_CONTENT + " UPDATED")
                            .withStatus(WpPostStatus.DRAFT)
                            .build();

            final WpPost post = adminClient.posts().update(existingPostId, updateRequest);

            // THEN
            // TODO: fix assertions
            assertThat(post)
                    .isNotNull()
                    .satisfies(p -> {
                        assertThat(p.getId()).isEqualTo(existingPostId);
                        assertThat(p.getStatus()).isEqualTo(WpPostStatus.DRAFT);
                        assertThat(p.getTitle().getRaw()).isNotNull().isEqualTo(POST_1_TITLE + " UPDATED");
                        assertThat(p.getContent().getRaw()).isNotNull().isEqualTo(POST_1_CONTENT + " UPDATED");
                        assertThat(p.getExcerpt().getRaw()).isNotNull().isEqualTo(POST_1_CONTENT + " UPDATED");
                    });
        }

        private String linkForPost(final @NonNull Long id) {
            return String.format("%s/?p=%d", getHttpsBaseUrl(), id);
        }

        private String linkForPost(final @NonNull String slug) {
            return String.format("%s/%s/", getHttpsBaseUrl(), slug);
        }
    }

    @DisplayName("Post Types APIs - Integration Tests")
    @Nested
    class PostTypeTests {

        @DisplayName("'GET' works")
        @Test
        void get__works() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            final WpPostType postType = adminClient.postTypes().get("post");

            // THEN
            assertThat(postType).isNotNull();
            assertThat(postType.getName()).isEqualTo("Posts");
            assertThat(postType.getDescription()).isEmpty();
            assertThat(postType.getSlug()).isEqualTo("post");
            assertThat(postType.getTaxonomies()).containsExactlyInAnyOrder(CATEGORY, POST_TAG);
        }

        @DisplayName("'LIST' works")
        @Test
        void list__works() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            final Map<String, WpPostType> response = adminClient.postTypes().list();

            // THEN
            assertThat(response)
                    .containsKeys(
                            "post",
                            "page",
                            "attachment",
                            "nav_menu_item",
                            "wp_block",
                            "wp_template",
                            "wp_navigation"); // we might have others, but we are not testing that here
        }
    }

    @DisplayName("Post Status APIs - Integration Tests")
    @Nested
    class StatusTests {

        @DisplayName("'LIST' works")
        @Test
        void list__works_with_just_paging() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            final Map<String, WpStatus> response = adminClient.postStatuses().list();

            // THEN
            assertThat(response)
                    .containsKeys(
                            "publish", "future", "draft", "pending", "private", "trash");
        }
    }

    @DisplayName("Tag APIs - Integration Tests")
    @Nested
    class TagTests {

        private static final String TAG_1_NAME = "Tag #1";
        private static final String TAG_1_DESCRIPTION = "My first tag";
        private static final String TAG_1_SLUG = "tag-1";

        private static final String TAG_2_NAME = "Tag #2";
        private static final String TAG_2_DESCRIPTION = "My second tag";
        private static final String TAG_2_SLUG = "tag-2";

        @DisplayName("'CREATE' fails on duplicate")
        @Test
        void create__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // given a tag already exists
            final Long existingTagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN/THEN
            final WpTagCreateUpdateRequest creationRequest =
                    tagCreateUpdateRequest()
                            .withName(TAG_1_NAME)
                            .build();

            assertThrowsWpBadRequest(() -> adminClient.tags().create(creationRequest),
                    "term_exists",
                    "A term with the name provided already exists in this taxonomy.",
                    Map.of("term_id", existingTagId.intValue()));
        }

        @DisplayName("'CREATE' works")
        @Test
        void create__works() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            final WpTag tag = adminClient.tags().create(tagCreateUpdateRequest()
                    .withName(TAG_1_NAME)
                    .withDescription(TAG_1_DESCRIPTION)
                    .withSlug(TAG_1_SLUG)
                    .build());

            // THEN
            WordPressAssertions.assertThat(tag)
                               .isNotNull()
                               .hasNonZeroId()
                               .hasName(TAG_1_NAME)
                               .hasDescription(TAG_1_DESCRIPTION)
                               .hasSlug(TAG_1_SLUG)
                               .hasCount(0)
                               .hasTaxonomy(WpTaxonomy.POST_TAG)
                               .hasLink(linkForTag(TAG_1_SLUG));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void delete__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.tags().delete(1000L), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long tagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN
            final WpTagDeletionResponse deletionResponse = adminClient.tags().delete(tagId);

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(tagId)
                                              .hasCount(0)
                                              .hasDescription(TAG_1_DESCRIPTION)
                                              .hasName(TAG_1_NAME)
                                              .hasSlug(TAG_1_SLUG)
                                              .hasLink(linkForTag(TAG_1_SLUG))
                                              .hasTaxonomy(POST_TAG));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void get__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();
            final Long tagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.tags().get(tagId + 1000, null), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'GET' works with context")
        @Test
        void get__works_with_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long tagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN
            final WpTag tag = adminClient.tags().get(tagId, WpContext.EDIT);

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

        @DisplayName("'GET' works with no context")
        @Test
        void get__works_with_no_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long tagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN
            final WpTag tag = adminClient.tags().get(tagId, null);

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

        @DisplayName("'LIST' works with just paging")
        @Test
        void list__works_with_just_paging() {

            // GIVEN
            wpCleanDefaultData();

            final Long cat1_id = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);
            final Long cat2_id = givenTagExists(TAG_2_NAME, TAG_2_DESCRIPTION, TAG_2_SLUG);

            // WHEN
            final WpPagedResponse<WpTag> response = adminClient.tags().list(new WpPaginationQuery(1, 10), null);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(2)
                               .doesNotHaveNextPage();

            assertThat(response.getItems())
                    .isNotNull()
                    .hasSize(2)
                    .extracting(
                            WpTag::getId,
                            WpTag::getName,
                            WpTag::getDescription,
                            WpTag::getSlug,
                            WpTag::getTaxonomy
                    )
                    .containsExactly(
                            tuple(cat1_id, TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG, POST_TAG),
                            tuple(cat2_id, TAG_2_NAME, TAG_2_DESCRIPTION, TAG_2_SLUG, POST_TAG)
                    );
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void list__works_with_paging_and_query() {

            // GIVEN
            wpCleanDefaultData();

            final Long cat1_id = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);
            final Long cat2_id = givenTagExists(TAG_2_NAME, TAG_2_DESCRIPTION, TAG_2_SLUG);

            // Filter just for tag #2
            final WpTagQuery tagQuery = WpTagQuery.builder()
                                                  .withSlug(TAG_2_SLUG)
                                                  .build();

            // WHEN
            final WpPagedResponse<WpTag> response = adminClient.tags().list(new WpPaginationQuery(1, 10), tagQuery);

            // THEN
            WordPressAssertions.assertThat(response)
                               .isNotNull()
                               .hasPageNumber(1)
                               .hasItemsPerPage(10)
                               .hasTotalPages(1)
                               .hasTotalItems(1)
                               .doesNotHaveNextPage();

            assertThat(response.getItems())
                    .isNotNull()
                    .hasSize(1)
                    .extracting(
                            WpTag::getId,
                            WpTag::getName,
                            WpTag::getDescription,
                            WpTag::getSlug,
                            WpTag::getTaxonomy
                    )
                    .containsExactly(
                            tuple(cat2_id, TAG_2_NAME, TAG_2_DESCRIPTION, TAG_2_SLUG, POST_TAG)
                    );
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void update__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();
            long nonExistingTagId = 1000L;

            // WHEN/THEN
            final WpTagCreateUpdateRequest updateRequest = tagCreateUpdateRequest().build();
            assertThrowsWpNotFound(() -> adminClient.tags().update(nonExistingTagId, updateRequest), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'UPDATE' works")
        @Test
        void update__works() {

            // GIVEN
            wpCleanDefaultData();

            final Long tagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN
            final WpTagCreateUpdateRequest updateRequest =
                    tagCreateUpdateRequest()
                            .withName(TAG_1_NAME + " UPDATED")
                            .withDescription(TAG_1_DESCRIPTION + " UPDATED")
                            .withSlug(TAG_1_SLUG + " UPDATED")
                            .build();

            final WpTag tag = adminClient.tags().update(tagId, updateRequest);

            // THEN
            WordPressAssertions.assertThat(tag)
                               .isNotNull()
                               .hasId(tagId)
                               .hasCount(0)
                               .hasDescription(TAG_1_DESCRIPTION + " UPDATED")
                               .hasName(TAG_1_NAME + " UPDATED")
                               .hasSlug(toWordPressSlug(TAG_1_SLUG + " UPDATED"))
                               .hasTaxonomy(POST_TAG);
        }

        private String linkForTag(final @NonNull String slug) {
            return String.format("%s/tag/%s/", getHttpsBaseUrl(), slug);
        }
    }

    @DisplayName("Comment APIs - Integration Tests")
    @Nested
    class CommentTests {

        @DisplayName("'CREATE' fails on non-existing parent post")
        @Test
        void create__fails_on_non_existing_parent_post() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            final WpCommentCreateUpdateRequest createUpdateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withPostId(1000L) // NON EXISTENT POST
                                                .withContent("This is my comment")
                                                .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> adminClient.comments().create(createUpdateRequest),
                    "rest_comment_invalid_post_id",
                    "Sorry, you are not allowed to create this comment without a post.");
        }

        @DisplayName("'CREATE' works")
        @Test
        void create__works() {

            // GIVEN
            wpCleanDefaultData();

            final Long existingPostId = givenPostExists("My post", "My content", WpPostStatus.PUBLISH);

            // WHEN
            WpCommentCreateUpdateRequest createUpdateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withPostId(existingPostId)
                                                .withContent("My Comment")
                                                .build();

            final WpComment comment = adminClient.comments().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId()
                               .hasPost(existingPostId)
                               .hasContentSatisfying(c -> c.hasRaw("My Comment"));
        }

        @DisplayName("'CREATE' works with password")
        @Test
        void create__works_with_password() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists("My post", "My content", WpPostStatus.PUBLISH);

            // WHEN
            WpCommentCreateUpdateRequest createUpdateRequest =
                    WpCommentCreateUpdateRequest.builder()
                                                .withPostId(existingPostId)
                                                .withContent("My Comment")
                                                .withPassword("mypassword")
                                                .build();

            final WpComment comment = adminClient.comments().create(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId()
                               .hasPost(existingPostId)
                               .hasContentSatisfying(c -> c.hasRaw("My Comment"));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void delete__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.comments().delete(1000L), "rest_comment_invalid_id", "Invalid comment ID.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists("My post", "My content", WpPostStatus.PUBLISH);
            final Long existingCommentId = givenCommentExists(existingPostId, "My Comment", "enrico", "enrico@some.email.com");

            // WHEN
            final WpCommentDeletionResponse deletionResponse = adminClient.comments().delete(existingCommentId);

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(existingCommentId)
                               );
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void get__fails_on_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.comments().get(1000L, null), "rest_comment_invalid_id", "Invalid comment ID.");
        }

        @DisplayName("'GET' works")
        @Test
        void get__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long existingPostId = givenPostExists("My post", "My content", WpPostStatus.PUBLISH);
            final Long existingCommentId = givenCommentExists(existingPostId, "My Comment", "enrico", "enrico@some.email.com");

            // WHEN
            final WpComment comment = adminClient.comments().get(existingCommentId, WpContext.EDIT);

            // THEN
            WordPressAssertions.assertThat(comment)
                               .isNotNull()
                               .hasId(existingCommentId)
                               .hasAuthorName("enrico")
                               .hasContentSatisfying(c -> c.hasRaw("My Comment"));
        }

        @DisplayName("'TRASH' fails on HTTP NOT FOUND")
        @Test
        void trash__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.comments().trash(1000L), "rest_comment_invalid_id", "Invalid comment ID.");
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void update__fails_on_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.comments().update(1000L, WpCommentCreateUpdateRequest.builder().build()),
                    "rest_comment_invalid_id", "Invalid comment ID.");
        }
    }

    @DisplayName("Taxonomies APIs - Integration Tests")
    @Nested
    class TaxonomyTests {

        @DisplayName("'GET' works")
        @Test
        void get__works() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            final WpTaxonomyInfo taxonomy = adminClient.taxonomies().get("post_tag");

            // THEN
            assertThat(taxonomy).isNotNull();
            assertThat(taxonomy.getName()).isEqualTo("Tags");
            assertThat(taxonomy.getDescription()).isEmpty();
            assertThat(taxonomy.getSlug()).isEqualTo("post_tag");
        }

        @DisplayName("'LIST' works")
        @Test
        void list__works() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN
            final Map<String, WpTaxonomyInfo> response = adminClient.taxonomies().list();

            // THEN
            assertThat(response)
                    .containsKeys(
                            "category",
                            "post_tag",
                            "nav_menu",
                            "wp_pattern_category"); // we might have others, but we are not testing that here
        }
    }
}
