package io.github.evisentin.wordpress.test.integration.base;

import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.assertions.WordPressAssertions;
import io.github.evisentin.wordpress.client.domain.model.*;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.enums.WpOpenClosed;
import io.github.evisentin.wordpress.client.domain.model.enums.WpTagOrderFields;
import io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy;
import io.github.evisentin.wordpress.client.domain.model.query.WpCategoryQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpTagQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpMediaUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpTagCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpMediaDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpTagDeletionResponse;
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
import java.util.Map;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.enums.WpOpenClosed.CLOSED;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpOpenClosed.OPEN;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PRIVATE;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PUBLISH;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.TRASH;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpSortDirection.ASC;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy.CATEGORY;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy.POST_TAG;
import static io.github.evisentin.wordpress.test.integration.base.SlugUtils.toWordPressSlug;
import static io.github.evisentin.wordpress.test.integration.base.WpAssertions.assertThrowsWpBadRequest;
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

        log.info("installWordpress: BEGIN");

        if (!wpIsWordPressInstalled()) {
            log.info("installWordpress: initializing...");
            wpInitWordPress(getHttpsBaseUrl());
            wpConfigureDefaultUsers();
            wpActivatePermalinks();
            wpCleanDefaultData();
            log.info("installWordpress: initialized");
        }

        log.info("installWordpress: END");

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

    private Long givenTagExists(final String name, final String description, final String slug) {
        return wpCreateTag(name, description, slug);
    }

    private static WpCategoryCreateUpdateRequest.WpCategoryCreateUpdateRequestBuilder categoryCreateUpdateRequest() {
        return WpCategoryCreateUpdateRequest.builder();
    }

    private static WpPostCreateUpdateRequest.WpPostCreateUpdateRequestBuilder postCreateUpdateRequest() {
        return WpPostCreateUpdateRequest.builder();
    }

    private static WpTagCreateUpdateRequest.WpTagCreateUpdateRequestBuilder tagCreateUpdateRequest() {
        return WpTagCreateUpdateRequest.builder();
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
            final WpMedia media = adminClient.createMedia(file, "sample.png", "image/png");
            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull()
                               .hasNonZeroId()
                               .hasSlugStartingWith("sample")
                               .hasType("attachment")
                               .hasTitleSatisfying(t ->
                                       t.hasRaw("sample")
                                        .hasRendered("sample"))
                               .hasAuthorId(1L)
                               .hasCommentStatus(WpOpenClosed.OPEN)
                               .hasPingStatus(WpOpenClosed.CLOSED)
                               .hasMediaType("image")
                               .hasMimeType("image/png");
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void delete__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.deleteMedia(1000L), "rest_post_invalid_id", "Invalid post ID.");
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
            final WpMedia existingMedia = adminClient.createMedia(file, "sample.png", "image/png");

            // WHEN
            final WpMediaDeletionResponse deletionResponse = adminClient.deleteMedia(existingMedia.getId());

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
            assertThrowsWpNotFound(() -> adminClient.getMedia(10L, null), "rest_post_invalid_id", "Invalid post ID.");
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
            final WpMedia existingMedia = adminClient.createMedia(file, "sample.png", "image/png");

            // WHEN
            final WpMedia media = adminClient.getMedia(existingMedia.getId(), WpContext.EDIT);

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
            final WpMedia existingMedia = adminClient.createMedia(file, "sample.png", "image/png");

            // WHEN
            final WpMedia media = adminClient.getMedia(existingMedia.getId(), null);

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
            Long nonExistingMediaId = 9L;

            // WHEN/THEN
            final WpMediaUpdateRequest updateRequest = WpMediaUpdateRequest.builder().build();
            assertThrowsWpNotFound(() -> adminClient.updateMedia(nonExistingMediaId, updateRequest), "rest_post_invalid_id", "Invalid post ID.");
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
            final WpMedia existingMedia = adminClient.createMedia(file, "sample.png", "image/png");

            // WHEN
            final WpMediaUpdateRequest updateRequest =
                    WpMediaUpdateRequest.builder()
                                        .withTitle("Title updated")
                                        .withDescription("Description updated")
                                        .withSlug("Slug updated")
                                        .build();

            final WpMedia media = adminClient.updateMedia(existingMedia.getId(), updateRequest);

            // THEN
            WordPressAssertions.assertThat(media)
                               .isNotNull()
                               .hasId(existingMedia.getId())
                               .hasDescriptionSatisfying(t -> t.hasRaw("Description updated"))
                               .hasTitleSatisfying(t -> t.hasRaw("Title updated"))
                               .hasSlug("slug-updated");
        }
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

            assertThrowsWpBadRequest(() -> adminClient.createCategory(creationRequest),
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
            final WpCategory category = adminClient.createCategory(categoryCreateUpdateRequest()
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
            assertThrowsWpNotFound(() -> adminClient.deleteCategory(1000L), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);

            // WHEN
            final WpCategoryDeletionResponse deletionResponse = adminClient.deleteCategory(categoryId);

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
            assertThrowsWpNotFound(() -> adminClient.getCategory(categoryId + 1000, null), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'GET' works with context")
        @Test
        void get__works_with_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = givenCategoryExists(CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_SLUG);

            // WHEN
            final WpCategory category = adminClient.getCategory(categoryId, WpContext.EDIT);

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
            final WpCategory category = adminClient.getCategory(categoryId, null);

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
            final WpPagedResponse<WpCategory> response = adminClient.listCategories(WpPagingQuery.of(1, 10), null);

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
            final WpPagedResponse<WpCategory> response = adminClient.listCategories(WpPagingQuery.of(1, 10), categoryQuery);

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
            Long nonExistingCategoryId = 1000L;

            // WHEN/THEN
            final WpCategoryCreateUpdateRequest updateRequest = categoryCreateUpdateRequest().build();
            assertThrowsWpNotFound(() -> adminClient.updateCategory(nonExistingCategoryId, updateRequest), "rest_term_invalid", "Term does not exist.");
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

            assertThrowsWpBadRequest(() -> adminClient.updateCategory(categoryId, updateRequest),
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

            final WpCategory category = adminClient.updateCategory(categoryId, updateRequest);

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

            assertThrowsWpBadRequest(() -> adminClient.createTag(creationRequest),
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
            final WpTag tag = adminClient.createTag(tagCreateUpdateRequest()
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
            assertThrowsWpNotFound(() -> adminClient.deleteTag(1000L), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long tagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN
            final WpTagDeletionResponse deletionResponse = adminClient.deleteTag(tagId);

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
            assertThrowsWpNotFound(() -> adminClient.getTag(tagId + 1000, null), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'GET' works with context")
        @Test
        void get__works_with_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long tagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN
            final WpTag tag = adminClient.getTag(tagId, WpContext.EDIT);

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
            final WpTag tag = adminClient.getTag(tagId, null);

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
            final WpPagedResponse<WpTag> response = adminClient.listTags(WpPagingQuery.of(1, 10), null);

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
            final WpPagedResponse<WpTag> response = adminClient.listTags(WpPagingQuery.of(1, 10), tagQuery);

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
            Long nonExistingTagId = 1000L;

            // WHEN/THEN
            final WpTagCreateUpdateRequest updateRequest = tagCreateUpdateRequest().build();
            assertThrowsWpNotFound(() -> adminClient.updateTag(nonExistingTagId, updateRequest), "rest_term_invalid", "Term does not exist.");
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

            final WpTag tag = adminClient.updateTag(tagId, updateRequest);

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
                            .withCategories(Set.of(categoryNews))
                            .withTags(Set.of(tagCH))
                            .build();

            final WpPost post = adminClient.createPost(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasLink(linkForPost(post.getId()))
                               .hasSlug("")
                               .hasGeneratedSlug(toWordPressSlug(POST_1_TITLE))
                               .hasStatus(DRAFT)
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
                            .withCategories(Set.of(nonExistingCategory1, nonExistingCategory2, nonExistingCategory3))
                            .withTags(Set.of(nonExistingTag1, nonExistingTag2, nonExistingTag3))
                            .withStatus(PUBLISH)
                            .withCommentStatus(WpOpenClosed.CLOSED)
                            .withPingStatus(WpOpenClosed.CLOSED)
                            .build();

            final WpPost post = adminClient.createPost(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasLink(linkForPost(post.getSlug())) // not draft, the link has the slug
                               .hasSlug(toWordPressSlug(POST_1_TITLE))
                               .hasGeneratedSlug(toWordPressSlug(POST_1_TITLE))
                               .hasStatus(PUBLISH)
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
                            .withCategories(Set.of(categoryNews))
                            .withTags(Set.of(tagCH))
                            .withPassword("my password")
                            .withStatus(PUBLISH)
                            .build();

            final WpPost post = adminClient.createPost(createUpdateRequest);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasLink(linkForPost(post.getSlug())) // not draft, the link has the slug
                               .hasSlug(toWordPressSlug(POST_1_TITLE))
                               .hasGeneratedSlug(toWordPressSlug(POST_1_TITLE))
                               .hasStatus(PUBLISH)
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
            assertThrowsWpNotFound(() -> adminClient.deletePost(1000L), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();

            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withStatus(PUBLISH)
                            .build();

            final WpPost existingPost = adminClient.createPost(createUpdateRequest);

            // WHEN
            final WpPostDeletionResponse deletionResponse = adminClient.deletePost(existingPost.getId());

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(existingPost.getId())
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
                                              .hasStatus(existingPost.getStatus()));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void get__fails_on_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.getPost(1000L, null), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'GET' works with password")
        @Test
        void get__gets_content_on_password_protected_post_and_password() {

            // GIVEN
            wpCleanDefaultData();

            final String PASSWORD = "password$$";

            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withStatus(PUBLISH)
                            .withPassword(PASSWORD)
                            .build();

            final WpPost existingPost = adminClient.createPost(createUpdateRequest);

            // WHEN
            final WpPost post = standardUserClient.getPost(existingPost.getId(), WpContext.VIEW, PASSWORD);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasStatus(PUBLISH)
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
            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withStatus(PUBLISH)
                            .withPassword("password$$")
                            .build();

            final WpPost existingPost = adminClient.createPost(createUpdateRequest);

            // WHEN
            final WpPost post = standardUserClient.getPost(existingPost.getId(), WpContext.VIEW);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasId()
                               .hasStatus(PUBLISH)
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
            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withStatus(PUBLISH)
                            .build();

            final WpPost existingPost = adminClient.createPost(createUpdateRequest);

            // WHEN
            final WpPost post = adminClient.getPost(existingPost.getId(), WpContext.EDIT);

            // THEN
            assertThat(post).isNotNull();
            assertThat(post.getId()).isNotNull().isEqualTo(existingPost.getId());
        }

        @DisplayName("'GET' works without context")
        @Test
        void get__works_with_no_context() {

            // GIVEN
            wpCleanDefaultData();
            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withStatus(PUBLISH)
                            .build();

            final WpPost existingPost = adminClient.createPost(createUpdateRequest);

            // WHEN
            final WpPost post = adminClient.getPost(existingPost.getId(), null);

            // THEN
            assertThat(post).isNotNull();
            assertThat(post.getId()).isNotNull().isEqualTo(existingPost.getId());
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void list__works_with_just_paging() {

            // GIVEN
            wpCleanDefaultData();
            final WpPost existingPost = adminClient.createPost(postCreateUpdateRequest()
                    .withTitle(POST_1_TITLE)
                    .withStatus(PUBLISH)
                    .build());

            // WHEN
            final WpPagedResponse<WpPost> response = adminClient.listPosts(WpPagingQuery.of(1, 10), null);

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
                                               .hasId(existingPost.getId())
                                               .hasLink(existingPost.getLink())
                                               .hasSlug(existingPost.getSlug())
                                               .hasStatus(PUBLISH)
                                               .hasType(existingPost.getType())
                                               // during listing we have just "rendered", not "raw"
                                               .hasTitleSatisfying(title -> title.hasRendered(existingPost.getTitle().getRendered()))

                                               // during listing we have just "rendered", not "raw"
                                               .hasContentSatisfying(content -> content.hasRendered(existingPost.getContent().getRendered()))
                                               // during listing we have just "rendered", not "raw"
                                               .hasExcerptSatisfying(excerpt -> excerpt.hasRendered(existingPost.getExcerpt().getRendered()))
                    );
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void list__works_with_paging_and_query() {

            // GIVEN
            wpCleanDefaultData();
            final WpPost draft_post = adminClient.createPost(postCreateUpdateRequest()
                    .withTitle(POST_1_TITLE)
                    .withStatus(DRAFT)
                    .build());

            final WpPost private_post = adminClient.createPost(postCreateUpdateRequest()
                    .withTitle(POST_2_TITLE)
                    .withStatus(PRIVATE)
                    .build());

            final WpPost published_post = adminClient.createPost(postCreateUpdateRequest()
                    .withTitle(POST_3_TITLE)
                    .withStatus(PUBLISH)
                    .build());

            // WHEN
            final WpPostQuery postQuery = WpPostQuery.builder()
                                                     .withStatuses(Set.of(DRAFT, PRIVATE))
                                                     .withOrder(ASC)
                                                     .withOrderBy(WpTagOrderFields.ID)
                                                     .build();

            final WpPagedResponse<WpPost> response = adminClient.listPosts(WpPagingQuery.of(1, 10), postQuery);

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
                    .containsExactly(draft_post.getId(), private_post.getId());
        }

        @DisplayName("'TRASH' fails on HTTP NOT FOUND")
        @Test
        void trash__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.trashPost(1000L), "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'TRASH' works")
        @Test
        void trash__works() {

            // GIVEN
            wpCleanDefaultData();

            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withStatus(PUBLISH)
                            .build();

            final WpPost existingPost = adminClient.createPost(createUpdateRequest);

            // WHEN
            final WpPost deletionResponse = adminClient.trashPost(existingPost.getId());

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .hasId(existingPost.getId())
                               .hasStatus(TRASH);
        }

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void update__fails_on_not_found() {
            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.updatePost(1000L, postCreateUpdateRequest().build()),
                    "rest_post_invalid_id", "Invalid post ID.");
        }

        @DisplayName("'UPDATE' works")
        @Test
        void update__works() {

            // GIVEN
            wpCleanDefaultData();
            WpPostCreateUpdateRequest createUpdateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE)
                            .withContent(POST_1_CONTENT)
                            .withStatus(PUBLISH)
                            .build();

            final WpPost existingPost = adminClient.createPost(createUpdateRequest);

            // WHEN
            WpPostCreateUpdateRequest updateRequest =
                    postCreateUpdateRequest()
                            .withTitle(POST_1_TITLE + " UPDATED")
                            .withContent(POST_1_CONTENT + " UPDATED")
                            .withExcerpt(POST_1_CONTENT + " UPDATED")
                            .withStatus(DRAFT)
                            .build();

            final WpPost post = adminClient.updatePost(existingPost.getId(), updateRequest);

            // THEN
            // TODO: fix assertions
            assertThat(post)
                    .isNotNull()
                    .satisfies(p -> {
                        assertThat(p.getId()).isEqualTo(existingPost.getId());
                        assertThat(p.getStatus()).isEqualTo(DRAFT);
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

        private static String toBlock(final @NonNull String text) {
            return String.format("<p>%s</p>\n", text);
        }
    }
}
