package com.ev.wordpress.client.testsupport;

import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.assertions.WordPressAssertions;
import com.ev.wordpress.client.domain.dto.WpCategory;
import com.ev.wordpress.client.domain.dto.WpPagedResponse;
import com.ev.wordpress.client.domain.dto.WpPost;
import com.ev.wordpress.client.domain.dto.WpTag;
import com.ev.wordpress.client.domain.dto.enums.WpContext;
import com.ev.wordpress.client.domain.dto.enums.WpPostStatus;
import com.ev.wordpress.client.domain.dto.enums.WpTagOrderFields;
import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import com.ev.wordpress.client.domain.dto.query.WpCategoryQuery;
import com.ev.wordpress.client.domain.dto.query.WpPagingQuery;
import com.ev.wordpress.client.domain.dto.query.WpPostQuery;
import com.ev.wordpress.client.domain.dto.query.WpTagQuery;
import com.ev.wordpress.client.domain.dto.requests.WpCategoryCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.requests.WpPostCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.requests.WpTagCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.responses.WpCategoryDeletionResponse;
import com.ev.wordpress.client.domain.dto.responses.WpPostDeletionResponse;
import com.ev.wordpress.client.domain.dto.responses.WpTagDeletionResponse;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.ev.wordpress.client.domain.dto.enums.WpContext.EDIT;
import static com.ev.wordpress.client.domain.dto.enums.WpPostStatus.DRAFT;
import static com.ev.wordpress.client.domain.dto.enums.WpPostStatus.PRIVATE;
import static com.ev.wordpress.client.domain.dto.enums.WpPostStatus.PUBLISH;
import static com.ev.wordpress.client.domain.dto.enums.WpSortDirection.ASC;
import static com.ev.wordpress.client.domain.dto.enums.WpTaxonomy.CATEGORY;
import static com.ev.wordpress.client.testsupport.SlugUtils.toWordPressSlug;
import static com.ev.wordpress.client.testsupport.WpAssertions.assertThrowsWpBadRequest;
import static com.ev.wordpress.client.testsupport.WpAssertions.assertThrowsWpForbidden;
import static com.ev.wordpress.client.testsupport.WpAssertions.assertThrowsWpNotFound;
import static com.ev.wordpress.client.testsupport.WpAssertions.assertThrowsWpUnauthorized;

public abstract class AbstractBasicAuthenticationWpRestClientContractTest extends AbstractMockServerTest {
    private WpRestClient client;

    @BeforeEach
    void setUp() {
        this.client = client();
    }

    protected abstract WpBaseRestClient client();

    @DisplayName("'CATEGORY' Operations")
    @Nested
    class CategoryTests {

        private static final String TEST_CATEGORY_NAME = "my category";
        private static final String TEST_CATEGORY_DESCRIPTION = "my category description";
        private static final String TEST_CATEGORY_SLUG = "my-category";

        @DisplayName("'CREATE' fails on HTTP BAD REQUEST")
        @Test
        void createFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/category/create.failure.bad-request.json");

            final WpCategoryCreateUpdateRequest createRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(TEST_CATEGORY_NAME)
                                                 .withDescription(TEST_CATEGORY_DESCRIPTION)
                                                 .withSlug(TEST_CATEGORY_SLUG)
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
                                                 .withDescription(TEST_CATEGORY_DESCRIPTION)
                                                 .withSlug(TEST_CATEGORY_SLUG)
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
                                                 .withName(TEST_CATEGORY_NAME)
                                                 .withDescription(TEST_CATEGORY_DESCRIPTION)
                                                 .withSlug(TEST_CATEGORY_SLUG)
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
                                                 .withName(TEST_CATEGORY_NAME)
                                                 .withDescription(TEST_CATEGORY_DESCRIPTION)
                                                 .withSlug(TEST_CATEGORY_SLUG)
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
                                                 .withName(TEST_CATEGORY_NAME)
                                                 .withDescription(TEST_CATEGORY_DESCRIPTION)
                                                 .withSlug(TEST_CATEGORY_SLUG)
                                                 .withParentId(parent_id)
                                                 .build();

            final WpCategory category = client.createCategory(createRequest);

            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasParentId(parent_id)
                               .hasCount(0)
                               .hasDescription(TEST_CATEGORY_DESCRIPTION)
                               .hasName(TEST_CATEGORY_NAME).hasSlug(TEST_CATEGORY_SLUG)
                               .hasTaxonomy(CATEGORY)
                               .satisfies(cat -> {
                                           assertThat(cat.getId()).isNotNull();
                                           assertThat(cat.getLink()).isNotBlank().contains(PARENT_SLUG, TEST_CATEGORY_SLUG);
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
                                              .hasDescription(TEST_CATEGORY_DESCRIPTION)
                                              .hasName(TEST_CATEGORY_NAME)
                                              .hasSlug(TEST_CATEGORY_SLUG)
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
                               .hasDescription(TEST_CATEGORY_DESCRIPTION)
                               .hasName(TEST_CATEGORY_NAME)
                               .hasSlug(TEST_CATEGORY_SLUG)
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
                               .hasDescription(TEST_CATEGORY_DESCRIPTION)
                               .hasName(TEST_CATEGORY_NAME)
                               .hasSlug(TEST_CATEGORY_SLUG)
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
            assertThat(post).isNotNull();
            assertThat(post.getId()).isNotNull();
            assertThat(post.getLink()).isNotBlank();
            assertThat(post.getSlug()).isEmpty();
            assertThat(post.getGeneratedSlug()).isEqualTo(toWordPressSlug(TITLE));
            assertThat(post.getStatus()).isEqualTo(DRAFT);
            assertThat(post.getType()).isEqualTo("post");
            assertThat(post.getTitle())
                    .satisfies(f -> {
                        assertThat(f.getRaw()).isEqualTo(TITLE);
                        assertThat(f.getRendered()).isEqualTo(TITLE);
                    });
            assertThat(post.getContent())
                    .satisfies(f -> {
                        assertThat(f.getRaw()).isEqualTo(CONTENT);
                        assertThat(f.getRendered()).isEqualTo("<p>" + CONTENT + "</p>\n");
                        assertThat(f.getIsProtected()).isNotNull().isFalse();
                        assertThat(f.getBlockVersion()).isNotNull().isZero();
                    });

            assertThat(post.getExcerpt())
                    .satisfies(f -> {
                        assertThat(f.getRaw()).isEmpty();
                        assertThat(f.getRendered()).isEqualTo("<p>" + CONTENT + "</p>\n");
                        assertThat(f.getIsProtected()).isNotNull().isFalse();
                        assertThat(f.getBlockVersion()).isNull();
                    });

            assertThat(post.getCategories()).containsExactly(3L);
            assertThat(post.getTags()).containsExactly(2L);
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
            assertThat(post).isNotNull();
            assertThat(post.getId()).isNotNull();
            assertThat(post.getSlug()).isEqualTo(toWordPressSlug(TITLE));
            assertThat(post.getGeneratedSlug()).isEqualTo(toWordPressSlug(TITLE));
            assertThat(post.getStatus()).isEqualTo(PUBLISH);
            assertThat(post.getPassword()).isNotNull().isEqualTo("my password");
            assertThat(post.getType()).isEqualTo("post");
            assertThat(post.getTitle())
                    .satisfies(f -> {
                        assertThat(f.getRaw()).isEqualTo(TITLE);
                        assertThat(f.getRendered()).isEqualTo(TITLE);
                        assertThat(f.getIsProtected()).isNull();
                        assertThat(f.getBlockVersion()).isNull();
                    });
            assertThat(post.getContent())
                    .satisfies(f -> {
                        assertThat(f.getRaw()).isEqualTo(CONTENT);
                        assertThat(f.getRendered()).isEqualTo("<p>" + CONTENT + "</p>\n");
                        assertThat(f.getIsProtected()).isNotNull().isTrue();
                        assertThat(f.getBlockVersion()).isNotNull().isZero();
                    });

            assertThat(post.getExcerpt())
                    .satisfies(f -> {
                        assertThat(f.getRaw()).isEqualTo(CONTENT);
                        assertThat(f.getRendered()).isEqualTo("<p>" + CONTENT + "</p>\n");
                        assertThat(f.getIsProtected()).isNotNull().isTrue();
                        assertThat(f.getBlockVersion()).isNull();
                    });
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
            assertThat(response).isNotNull();
            assertThat(response.isDeleted()).isTrue();
            assertThat(response.getPrevious())
                    .satisfies(summary -> {
                        assertThat(summary).isNotNull();
                        assertThat(summary.getId()).isEqualTo(1005L);
                        assertThat(summary.getLink()).isNotBlank();

                        assertThat(summary.getTitle())
                                .isNotNull()
                                .satisfies(title -> {
                                    assertThat(title.getRaw()).isNotNull().isEqualTo("My post");
                                    assertThat(title.getRendered()).isNotNull().isEqualTo("My post");
                                });

                        assertThat(summary.getContent())
                                .isNotNull()
                                .satisfies(content -> {
                                    assertThat(content.getRaw()).isNotNull().isEqualTo("My Content");
                                    assertThat(content.getRendered()).isNotNull().isEqualTo("<p>My Content</p>\n");
                                });
                        assertThat(summary.getExcerpt())
                                .isNotNull()
                                .satisfies(exceprt -> {
                                    assertThat(exceprt.getRaw()).isNotNull().isEqualTo("My Content");
                                    assertThat(exceprt.getRendered()).isNotNull().isEqualTo("<p>My Content</p>\n");
                                });

                        assertThat(summary.getSlug()).isEqualTo("my-post");
                    });
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
            assertThat(post)
                    .isNotNull()
                    .satisfies(p -> {
                        assertThat(p.getId()).isEqualTo(id);
                        assertThat(p.getStatus()).isEqualTo(DRAFT);
                    });
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
            assertThat(post)
                    .isNotNull()
                    .satisfies(p -> {
                        assertThat(p.getId()).isEqualTo(id);
                        assertThat(p.getStatus()).isEqualTo(DRAFT);
                    });
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
            assertThat(post)
                    .isNotNull()
                    .satisfies(p -> {
                        assertThat(p.getId()).isEqualTo(id);
                        assertThat(p.getStatus()).isEqualTo(DRAFT);
                    });
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
            assertThat(response)
                    .isNotNull()
                    .satisfies(r -> {
                        assertThat(r.getPageNumber()).isEqualTo(1);
                        assertThat(r.getItemsPerPage()).isEqualTo(10);
                        assertThat(r.getTotalPages()).isEqualTo(1);
                        assertThat(r.getTotalItems()).isEqualTo(1);
                        assertThat(r.hasNextPage()).isFalse();
                        assertThat(r.getItems()).hasSize(1);
                        assertThat(r.getItems()).element(0).satisfies(p -> {
                            assertThat(p).isNotNull();
                            assertThat(p.getId()).isNotNull().isEqualTo(4L);
                            assertThat(p.getLink()).isEqualTo("https://localhost:61975/my-first-post/");
                            assertThat(p.getSlug()).isEqualTo("my-first-post");
                            assertThat(p.getStatus()).isEqualTo(PUBLISH);
                            assertThat(p.getType()).isEqualTo("post");
                            assertThat(p.getCategories()).containsExactly(1L);
                            assertThat(p.getTags()).isEmpty();

                            // during listing we have just "rendered", not "raw"
                            assertThat(p.getTitle().getRaw()).isNull();
                            assertThat(p.getTitle().getRendered()).isEqualTo("My first post");

                            // during listing we have just "rendered", not "raw"
                            assertThat(p.getContent().getRaw()).isNull();
                            assertThat(p.getContent().getRendered()).isEqualTo("");

                            // during listing we have just "rendered", not "raw"
                            assertThat(p.getExcerpt().getRaw()).isNull();
                            assertThat(p.getExcerpt().getRendered()).isEqualTo("");
                        });
                    });
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
            assertThat(response)
                    .isNotNull()
                    .satisfies(r -> {
                        assertThat(r.getPageNumber()).isEqualTo(1);
                        assertThat(r.getItemsPerPage()).isEqualTo(10);
                        assertThat(r.getTotalPages()).isEqualTo(1);
                        assertThat(r.getTotalItems()).isEqualTo(2);
                        assertThat(r.hasNextPage()).isFalse();
                        assertThat(r.getItems()).hasSize(2); // TWO POSTS (FILTER)
                        assertThat(r.getItems()).element(0).satisfies(p -> {
                            assertThat(p).isNotNull();
                            assertThat(p.getStatus()).isEqualTo(DRAFT);
                            assertThat(p.getId()).isNotNull().isEqualTo(4L);
                        });
                        assertThat(r.getItems()).element(1).satisfies(p -> {
                            assertThat(p).isNotNull();
                            assertThat(p.getStatus()).isEqualTo(PRIVATE);
                            assertThat(p.getId()).isNotNull().isEqualTo(5L);
                        });
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
            assertThat(post)
                    .isNotNull()
                    .satisfies(t -> {
                        assertThat(post.getStatus()).isNotNull().isEqualTo(WpPostStatus.TRASH);
                    });
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

            assertThat(post)
                    .isNotNull()
                    .satisfies(p -> {
                        assertThat(p.getId()).isEqualTo(4L);
                        assertThat(p.getStatus()).isEqualTo(DRAFT);
                        assertThat(p.getTitle().getRaw()).isNotNull().isEqualTo(TITLE + " UPDATED");
                        assertThat(p.getContent().getRaw()).isNotNull().isEqualTo(CONTENT + " UPDATED");
                        assertThat(p.getExcerpt().getRaw()).isNotNull().isEqualTo(CONTENT + " UPDATED");
                    });
        }
    }

    @DisplayName("'TAG' Operations")
    @Nested
    class TagTests {

        @DisplayName("'CREATE' fails on HTTP BAD REQUEST")
        @Test
        void createFailsOnBadRequest() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/create.failure.bad-request.json");

            final String NAME = "my tag";
            final String DESCRIPTION = "my description";
            final String SLUG = "my-tag";

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
            final String DESCRIPTION = "my description";
            final String SLUG = "my-tag";

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
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

            final String NAME = "my tag";
            final String DESCRIPTION = "my description";
            final String SLUG = "my-tag";

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
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

            final String NAME = "my tag";
            final String DESCRIPTION = "my description";
            final String SLUG = "my-tag";

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
                                            .build();

            // WHEN/THEN
            assertThrowsWpUnauthorized(() -> client.createTag(createRequest));
        }

        @DisplayName("'CREATE' works")
        @Test
        void createWorks() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/create.success.json");

            final String NAME = "my tag";
            final String DESCRIPTION = "my description";
            final String SLUG = "my-tag";

            final WpTagCreateUpdateRequest createRequest =
                    WpTagCreateUpdateRequest.builder()
                                            .withName(NAME)
                                            .withDescription(DESCRIPTION)
                                            .withSlug(SLUG)
                                            .build();

            // WHEN
            final WpTag tag = client.createTag(createRequest);

            // THEN
            assertThat(tag).isNotNull();
            assertThat(tag.getId()).isNotNull();
            assertThat(tag.getCount()).isEqualTo(0);
            assertThat(tag.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(tag.getLink()).isNotBlank().contains(SLUG);
            assertThat(tag.getName()).isEqualTo(NAME);
            assertThat(tag.getSlug()).isEqualTo(SLUG);
            assertThat(tag.getTaxonomy()).isNotNull().isEqualTo(WpTaxonomy.POST_TAG);
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
            assertThat(response).isNotNull();
            assertThat(response.isDeleted()).isTrue();
            assertThat(response.getPrevious())
                    .satisfies(summary -> {
                        assertThat(summary).isNotNull();
                        assertThat(summary.getId()).isEqualTo(1005L);
                        assertThat(summary.getCount()).isZero();
                        assertThat(summary.getDescription()).isEqualTo("Tag #1");
                        assertThat(summary.getLink()).isNotBlank();
                        assertThat(summary.getName()).isEqualTo("tag1");
                        assertThat(summary.getSlug()).isEqualTo("tag-1");
                        assertThat(summary.getTaxonomy()).isEqualTo(WpTaxonomy.POST_TAG);
                    });
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
            assertThat(tag).isNotNull();
            assertThat(tag.getId()).isEqualTo(tagId);
            assertThat(tag.getCount()).isEqualTo(0);
            assertThat(tag.getDescription()).isEqualTo("Tag #1");
            assertThat(tag.getLink()).isEqualTo("http://localhost:58033/tag/tag-1/");
            assertThat(tag.getName()).isEqualTo("tag1");
            assertThat(tag.getSlug()).isEqualTo("tag-1");
            assertThat(tag.getTaxonomy()).isNotNull().isEqualTo(WpTaxonomy.POST_TAG);
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
            assertThat(tag).isNotNull();
            assertThat(tag.getId()).isEqualTo(tagId);
            assertThat(tag.getCount()).isEqualTo(0);
            assertThat(tag.getDescription()).isEqualTo("Tag #1");
            assertThat(tag.getLink()).isEqualTo("http://localhost:58033/tag/tag-1/");
            assertThat(tag.getName()).isEqualTo("tag1");
            assertThat(tag.getSlug()).isEqualTo("tag-1");
            assertThat(tag.getTaxonomy()).isNotNull().isEqualTo(WpTaxonomy.POST_TAG);
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
            assertThat(response)
                    .isNotNull()
                    .satisfies(r -> {
                        assertThat(r.getPageNumber()).isEqualTo(1);
                        assertThat(r.getItemsPerPage()).isEqualTo(10);
                        assertThat(r.getTotalPages()).isEqualTo(1);
                        assertThat(r.getTotalItems()).isEqualTo(2);
                        assertThat(r.hasNextPage()).isFalse();
                        assertThat(r.getItems())
                                .hasSize(2)
                                .satisfiesExactly(
                                        tag -> {
                                            assertThat(tag.getName()).isEqualTo("tag1");
                                            assertThat(tag.getSlug()).isEqualTo("tag-1");
                                            assertThat(tag.getDescription()).isEqualTo("Tag #1");
                                        },
                                        tag -> {
                                            assertThat(tag.getName()).isEqualTo("tag2");
                                            assertThat(tag.getSlug()).isEqualTo("tag-2");
                                            assertThat(tag.getDescription()).isEqualTo("Tag #2");
                                        }
                                );
                    });
        }

        @DisplayName("'LIST' works with paging and query")
        @Test
        void listWorksWithPagingAndQuery() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/list.success.paging-and-query.json");

            // WHEN
            final WpTagQuery tagQuery = WpTagQuery.builder()
                                                  .withSlug("tag-2")
                                                  .withExcludeIds(Set.of(2000L))
                                                  .build();

            final WpPagedResponse<WpTag> response = client.listTags(WpPagingQuery.of(1, 10), tagQuery);

            // THEN
            assertThat(response)
                    .isNotNull()
                    .satisfies(r -> {
                        assertThat(r.getPageNumber()).isEqualTo(1);
                        assertThat(r.getItemsPerPage()).isEqualTo(10);
                        assertThat(r.getTotalPages()).isEqualTo(1);
                        assertThat(r.getTotalItems()).isEqualTo(1);
                        assertThat(r.hasNextPage()).isFalse();
                        assertThat(r.getItems())
                                .hasSize(1) // just "tag-2"
                                .satisfiesExactly(
                                        tag -> {
                                            assertThat(tag.getName()).isEqualTo("tag2");
                                            assertThat(tag.getSlug()).isEqualTo("tag-2");
                                            assertThat(tag.getDescription()).isEqualTo("Tag #2");
                                        }
                                );
                    });
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
            assertThat(tag).isNotNull();
            assertThat(tag.getId()).isNotNull();
            assertThat(tag.getCount()).isEqualTo(0);
            assertThat(tag.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(tag.getLink()).isNotBlank().contains(SLUG);
            assertThat(tag.getName()).isEqualTo(NAME);
            assertThat(tag.getSlug()).isEqualTo(SLUG);
            assertThat(tag.getTaxonomy()).isNotNull().isEqualTo(WpTaxonomy.POST_TAG);
        }
    }
}
