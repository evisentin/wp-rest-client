package io.github.evisentin.wordpress.client.contract.test;

import io.github.evisentin.wordpress.client.domain.api.WpBaseRestClient;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.assertions.WordPressAssertions;
import io.github.evisentin.wordpress.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.WpTag;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.enums.WpTagOrderFields;
import io.github.evisentin.wordpress.client.domain.model.query.WpCategoryQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpTagQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpTagCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpTagDeletionResponse;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.github.evisentin.wordpress.client.contract.test.SlugUtils.toWordPressSlug;
import static io.github.evisentin.wordpress.client.contract.test.WpAssertions.assertThrowsWpBadRequest;
import static io.github.evisentin.wordpress.client.contract.test.WpAssertions.assertThrowsWpForbidden;
import static io.github.evisentin.wordpress.client.contract.test.WpAssertions.assertThrowsWpNotFound;
import static io.github.evisentin.wordpress.client.contract.test.WpAssertions.assertThrowsWpUnauthorized;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpContext.EDIT;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.DRAFT;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PRIVATE;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.PUBLISH;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus.TRASH;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpSortDirection.ASC;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy.CATEGORY;
import static io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy.POST_TAG;
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
            assertThrowsWpBadRequest(() -> client.createCategory(createRequest));
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
            assertThatThrownBy(() -> client.createCategory(createRequest))
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
            assertThrowsWpForbidden(() -> client.createCategory(createRequest));
        }

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.createCategory(null))
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
            assertThrowsWpUnauthorized(() -> client.createCategory(createRequest));
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

            final WpCategory category = client.createCategory(createRequest);

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
            assertThrowsWpForbidden(() -> client.deleteCategory(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.deleteCategory(1005L));
        }

        @DisplayName("'DELETE' fails on null ID")
        @Test
        void deleteFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.deleteCategory(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.deleteCategory(1005L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/delete.success.json");

            // WHEN
            final WpCategoryDeletionResponse response = client.deleteCategory(1005L);

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
            assertThrowsWpForbidden(() -> client.getCategory(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.getCategory(1005L, null));
        }

        @DisplayName("'GET' fails on null ID")
        @Test
        void getFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.getCategory(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.getCategory(1005L, null));
        }

        @DisplayName("'GET' works with context")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/get.success.with-context.json");

            final Long catId = 2L;

            // WHEN
            final WpCategory category = client.getCategory(catId, WpContext.VIEW);

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

            final Long catId = 2L;

            // WHEN
            final WpCategory category = client.getCategory(catId, null);

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
            assertThrowsWpForbidden(() -> client.listCategories(WpPagingQuery.of(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.listCategories(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("pageQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.listCategories(WpPagingQuery.of(1, 10), null));
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
            final WpPagedResponse<WpCategory> response = client.listCategories(WpPagingQuery.of(1, 10), null);

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
            final WpPagedResponse<WpCategory> response = client.listCategories(WpPagingQuery.of(1, 10), categoryQuery);

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
            assertThrowsWpBadRequest(() -> client.updateCategory(2L, updateRequest));
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
            assertThrowsWpForbidden(() -> client.updateCategory(2L, updateRequest));
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
            assertThrowsWpNotFound(() -> client.updateCategory(2L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null ID")
        @Test
        void updateFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.updateCategory(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.updateCategory(1000L, null))
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
            assertThrowsWpUnauthorized(() -> client.updateCategory(2L, updateRequest));
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
            final WpCategory category = client.updateCategory(2L, updateRequest);

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
                                             .withCategories(Set.of(3L))
                                             .withTags(Set.of(2L))
                                             .build();

            // WHEN/THEN
            assertThrowsWpBadRequest(() -> client.createPost(createRequest));
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
                                             .withCategories(Set.of(3L))
                                             .withTags(Set.of(2L))
                                             .build();

            // WHEN/THEN
            assertThrowsWpForbidden(() -> client.createPost(createRequest));
        }

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.createPost(null))
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
                                             .withCategories(Set.of(3L))
                                             .withTags(Set.of(2L))
                                             .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.createPost(createRequest));
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
                                             .withCategories(Set.of(3L))
                                             .withTags(Set.of(2L))
                                             .build();

            final WpPost post = client.createPost(createUpdateRequest);

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

            final WpPost post = client.createPost(createUpdateRequest);

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
            assertThrowsWpForbidden(() -> client.deletePost(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.deletePost(1005L));
        }

        @DisplayName("'DELETE' fails on null ID")
        @Test
        void deleteFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.deletePost(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.deletePost(1005L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/delete.success.json");

            // WHEN
            final WpPostDeletionResponse response = client.deletePost(1005L);

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
            assertThrowsWpForbidden(() -> client.getPost(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.getPost(1005L, null));
        }

        @DisplayName("'GET' fails on null ID")
        @Test
        void getFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.getPost(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");

            assertThatThrownBy(() -> client.getPost(null, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.getPost(1005L, null));
        }

        @DisplayName("'GET' works (with context)")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/get.success.with-context.json");

            final Long id = 2L;

            // WHEN
            final WpPost post = client.getPost(id, EDIT);

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

            final Long id = 2L;

            // WHEN
            final WpPost post = client.getPost(id, EDIT, "my-password");

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

            final Long id = 2L;

            // WHEN
            final WpPost post = client.getPost(id, null);

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
            assertThrowsWpForbidden(() -> client.listPosts(WpPagingQuery.of(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.listPosts(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("pageQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.listPosts(WpPagingQuery.of(1, 10), null));
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void listWorksWithJustPaging() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/list.success.just-paging.json");

            // WHEN
            final WpPagedResponse<WpPost> response = client.listPosts(WpPagingQuery.of(1, 10), null);

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
                                                     .withStatuses(Set.of(DRAFT, PRIVATE))
                                                     .withOrder(ASC)
                                                     .withOrderBy(WpTagOrderFields.ID)
                                                     .build();

            final WpPagedResponse<WpPost> response = client.listPosts(WpPagingQuery.of(1, 10), postQuery);

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
            assertThrowsWpForbidden(() -> client.trashPost(1005L));
        }

        @DisplayName("'TRASH' fails on HTTP NOT FOUND")
        @Test
        void trashFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/trash.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.trashPost(1005L));
        }

        @DisplayName("'TRASH' fails on null ID")
        @Test
        void trashFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.trashPost(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'TRASH' fails on HTTP UNAUTHORIZED")
        @Test
        void trashFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/trash.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.trashPost(1005L));
        }

        @DisplayName("'TRASH' works")
        @Test
        void trashWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/post/trash.success.json");

            // WHEN
            final WpPost post = client.trashPost(1005L);

            // THEN
            WordPressAssertions.assertThat(post)
                               .isNotNull()
                               .hasStatus(TRASH);
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
            assertThrowsWpBadRequest(() -> client.updatePost(4L, updateRequest));
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
            assertThrowsWpForbidden(() -> client.updatePost(4L, updateRequest));
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
            assertThrowsWpNotFound(() -> client.updatePost(4L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null ID")
        @Test
        void updateFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.updatePost(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.updatePost(1000L, null))
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
            assertThrowsWpUnauthorized(() -> client.updatePost(4L, updateRequest));
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
            val post = client.updatePost(4L, updateRequest);

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
            assertThrowsWpBadRequest(() -> client.createTag(createRequest));
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
            assertThatThrownBy(() -> client.createTag(createRequest))
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
            assertThrowsWpForbidden(() -> client.createTag(createRequest));
        }

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.createTag(null))
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
            assertThrowsWpUnauthorized(() -> client.createTag(createRequest));
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
            final WpTag tag = client.createTag(createRequest);

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
            assertThrowsWpForbidden(() -> client.deleteTag(1005L));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/delete.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.deleteTag(1005L));
        }

        @DisplayName("'DELETE' fails on null ID")
        @Test
        void deleteFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.deleteTag(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'DELETE' fails on HTTP UNAUTHORIZED")
        @Test
        void deleteFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/delete.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.deleteTag(1005L));
        }

        @DisplayName("'DELETE' works")
        @Test
        void deleteWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/delete.success.json");

            // WHEN
            final WpTagDeletionResponse response = client.deleteTag(1005L);

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
            assertThrowsWpForbidden(() -> client.getTag(1005L, null));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.failure.not-found.json");

            // WHEN/THEN
            assertThrowsWpNotFound(() -> client.getTag(1005L, null));
        }

        @DisplayName("'GET' fails on null ID")
        @Test
        void getFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.getTag(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'GET' fails on HTTP UNAUTHORIZED")
        @Test
        void getFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.getTag(1005L, null));
        }

        @DisplayName("'GET' works (with context)")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.success.with-context.json");

            final Long tagId = 4L;

            // WHEN
            final WpTag tag = client.getTag(tagId, EDIT);

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

            final Long tagId = 4L;

            // WHEN
            final WpTag tag = client.getTag(tagId, null);

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
            assertThrowsWpForbidden(() -> client.listTags(WpPagingQuery.of(1, 10), null));
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.listTags(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("pageQuery is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on HTTP UNAUTHORIZED")
        @Test
        void listFailsOnUnauthorized() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/list.failure.unauthorized.json");

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.listTags(WpPagingQuery.of(1, 10), null));
        }

        @DisplayName("'LIST' works with paging")
        @Test
        void listWorksWithJustPaging() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/list.success.just-paging.json");

            // WHEN
            final WpPagedResponse<WpTag> response = client.listTags(WpPagingQuery.of(1, 10), null);

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
                                                  .withExcludeIds(Set.of(2000L))
                                                  .build();

            final WpPagedResponse<WpTag> response = client.listTags(WpPagingQuery.of(1, 10), tagQuery);

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
            assertThrowsWpBadRequest(() -> client.updateTag(2L, updateRequest));
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
            assertThrowsWpForbidden(() -> client.updateTag(2L, updateRequest));
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
            assertThrowsWpNotFound(() -> client.updateTag(2L, updateRequest));
        }

        @DisplayName("'UPDATE' fails on null ID")
        @Test
        void updateFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.updateTag(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'UPDATE' fails on null request")
        @Test
        void updateFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.updateTag(1000L, null))
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
            assertThrowsWpUnauthorized(() -> client.updateTag(2L, updateRequest));
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
            final WpTag tag = client.updateTag(2L, updateRequest);

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
}
