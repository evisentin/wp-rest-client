package com.ev.wordpress.test.integration.base;

import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.assertions.WordPressAssertions;
import com.ev.wordpress.client.domain.dto.WpCategory;
import com.ev.wordpress.client.domain.dto.WpPagedResponse;
import com.ev.wordpress.client.domain.dto.WpTag;
import com.ev.wordpress.client.domain.dto.enums.WpContext;
import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import com.ev.wordpress.client.domain.dto.query.WpCategoryQuery;
import com.ev.wordpress.client.domain.dto.query.WpPagingQuery;
import com.ev.wordpress.client.domain.dto.query.WpTagQuery;
import com.ev.wordpress.client.domain.dto.requests.WpCategoryCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.requests.WpTagCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.responses.WpCategoryDeletionResponse;
import com.ev.wordpress.client.domain.dto.responses.WpTagDeletionResponse;
import com.ev.wordpress.test.integration.BaseWordPressIntegrationTest;
import com.ev.wordpress.test.integration.base.factory.WpRestClientFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.ev.wordpress.client.domain.dto.enums.WpTaxonomy.CATEGORY;
import static com.ev.wordpress.client.domain.dto.enums.WpTaxonomy.POST_TAG;
import static com.ev.wordpress.test.integration.base.SlugUtils.toWordPressSlug;
import static com.ev.wordpress.test.integration.base.WpAssertions.assertThrowsWpBadRequest;
import static com.ev.wordpress.test.integration.base.WpAssertions.assertThrowsWpNotFound;

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
 * <p>Concrete subclasses must provide a {@link WpRestClientFactory} implementation
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
 * @see WpRestClientFactory
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

    protected abstract WpRestClientFactory clientFactory();

    private Long givenCategoryExists(final String name, final String description, final String slug) {
        return wpCreateCategory(name, description, slug);
    }

    private Long givenTagExists(final String name, final String description, final String slug) {
        return wpCreateTag(name, description, slug);
    }

    @DisplayName("Category APIs - Integration Tests")
    @Nested
    class CategoryTests {

        private final static String CATEGORY_1_NAME = "Category #1";
        private final static String CATEGORY_1_DESCRIPTION = "My first category";
        private final static String CATEGORY_1_SLUG = "category-1";

        private final static String CATEGORY_2_NAME = "Category #2";
        private final static String CATEGORY_2_DESCRIPTION = "My second category";
        private final static String CATEGORY_2_SLUG = "category-2";

        @DisplayName("'CREATE' fails on parent not found")
        @Test
        void create__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            Long nonExistingParentId = 1000L;

            // WHEN/THEN

            final WpCategoryCreateUpdateRequest creationRequest =
                    WpCategoryCreateUpdateRequest.builder()
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
            final WpCategory category = adminClient.createCategory(WpCategoryCreateUpdateRequest.builder()
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
                            WpCategory::getTaxonomy
                    )
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
            final WpCategoryCreateUpdateRequest updateRequest = WpCategoryCreateUpdateRequest.builder().build();
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
            final WpCategoryCreateUpdateRequest updateRequest =
                    WpCategoryCreateUpdateRequest.builder()
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
                    WpCategoryCreateUpdateRequest.builder()
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

    @DisplayName("TAG APIs - Integration Tests")
    @Nested
    class TagTests {

        private final static String TAG_1_NAME = "Tag #1";
        private final static String TAG_1_DESCRIPTION = "My first tag";
        private final static String TAG_1_SLUG = "tag-1";

        private final static String TAG_2_NAME = "Tag #2";
        private final static String TAG_2_DESCRIPTION = "My second tag";
        private final static String TAG_2_SLUG = "tag-2";

        @DisplayName("'CREATE' fails on duplicate")
        @Test
        void create__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // given a tag already exists
            final Long existingTagId = givenTagExists(TAG_1_NAME, TAG_1_DESCRIPTION, TAG_1_SLUG);

            // WHEN/THEN
            final WpTagCreateUpdateRequest creationRequest =
                    WpTagCreateUpdateRequest.builder()
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
            final WpTag tag = adminClient.createTag(WpTagCreateUpdateRequest.builder()
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
            final WpTagCreateUpdateRequest updateRequest = WpTagCreateUpdateRequest.builder().build();
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
                    WpTagCreateUpdateRequest.builder()
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
}
