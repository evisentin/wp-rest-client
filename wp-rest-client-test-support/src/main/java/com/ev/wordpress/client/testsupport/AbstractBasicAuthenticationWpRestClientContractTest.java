package com.ev.wordpress.client.testsupport;

import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.dto.WpPagedResponse;
import com.ev.wordpress.client.domain.dto.WpTag;
import com.ev.wordpress.client.domain.dto.enums.WpContext;
import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import com.ev.wordpress.client.domain.dto.query.WpPagingQuery;
import com.ev.wordpress.client.domain.dto.query.WpTagQuery;
import com.ev.wordpress.client.domain.dto.requests.WpTagCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.responses.WpTagDeletionResponse;
import com.ev.wordpress.client.domain.exception.WpBadRequestException;
import com.ev.wordpress.client.domain.exception.WpForbiddenException;
import com.ev.wordpress.client.domain.exception.WpNotFoundException;
import com.ev.wordpress.client.domain.exception.WpUnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

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

        // TODO: 'CREATE' fails on HTTP BAD REQUEST
        // TODO: 'CREATE' fails on HTTP FORBIDDEN
        // TODO: 'CREATE' fails on HTTP UNAUTHORIZED
        // TODO: 'CREATE' works

        // TODO: 'DELETE' fails on HTTP NOT FOUND
        // TODO: 'DELETE' fails on HTTP FORBIDDEN
        // TODO: 'DELETE' fails on HTTP UNAUTHORIZED
        // TODO: 'DELETE' works

        // TODO: 'GET' fails on HTTP NOT FOUND
        // TODO: 'GET' fails on HTTP FORBIDDEN
        // TODO: 'GET' fails on HTTP UNAUTHORIZED
        // TODO: 'GET' works

        // TODO: 'LIST' fails on HTTP FORBIDDEN
        // TODO: 'LIST' fails on HTTP UNAUTHORIZED
        // TODO: 'LIST' works

        // TODO: 'UPDATE' fails on HTTP BAD REQUEST
        // TODO: 'UPDATE' fails on HTTP FORBIDDEN
        // TODO: 'UPDATE' fails on HTTP NOT FOUND
        // TODO: 'UPDATE' fails on HTTP UNAUTHORIZED
        // TODO: 'UPDATE' works

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.createCategory(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");
        }

        @DisplayName("'DELETE' fails on null ID")
        @Test
        void deleteFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.deleteCategory(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'GET' fails on null ID")
        @Test
        void getFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.getCategory(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
        }

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.listCategories(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("pageQuery is marked non-null but is null");
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
    }

    @DisplayName("'POST' Operations")
    @Nested
    class PostTests {

        // TODO: 'CREATE' fails on HTTP BAD REQUEST
        // TODO: 'CREATE' fails on HTTP FORBIDDEN
        // TODO: 'CREATE' fails on HTTP UNAUTHORIZED
        // TODO: 'CREATE' works

        // TODO: 'DELETE' fails on HTTP NOT FOUND
        // TODO: 'DELETE' fails on HTTP FORBIDDEN
        // TODO: 'DELETE' fails on HTTP UNAUTHORIZED
        // TODO: 'DELETE' works

        // TODO: 'TRASH' fails on null ID
        // TODO: 'TRASH' fails on HTTP NOT FOUND
        // TODO: 'TRASH' fails on HTTP FORBIDDEN
        // TODO: 'TRASH' fails on HTTP UNAUTHORIZED
        // TODO: 'TRASH' works

        // TODO: 'GET' fails on HTTP NOT FOUND
        // TODO: 'GET' fails on HTTP FORBIDDEN
        // TODO: 'GET' fails on HTTP UNAUTHORIZED
        // TODO: 'GET' works

        // TODO: 'LIST' fails on HTTP FORBIDDEN
        // TODO: 'LIST' fails on HTTP UNAUTHORIZED
        // TODO: 'LIST' works

        // TODO: 'UPDATE' fails on HTTP BAD REQUEST
        // TODO: 'UPDATE' fails on HTTP FORBIDDEN
        // TODO: 'UPDATE' fails on HTTP NOT FOUND
        // TODO: 'UPDATE' fails on HTTP UNAUTHORIZED
        // TODO: 'UPDATE' works

        @DisplayName("'CREATE' fails on null request")
        @Test
        void createFailsOnNullRequest() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.createPost(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("creationRequest is marked non-null but is null");
        }

        @DisplayName("'DELETE' fails on null ID")
        @Test
        void deleteFailsOnNullId() {

            // WHEN/THEN
            assertThatThrownBy(() -> client.deletePost(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("id is marked non-null but is null");
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

        @DisplayName("'LIST' fails on null pageQuery")
        @Test
        void listFailsOnNullPaging() {
            // WHEN/THEN
            assertThatThrownBy(() -> client.listPosts(null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("pageQuery is marked non-null but is null");
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
    }

    @DisplayName("'TAG' Operations")
    @Nested
    class TagTests {

        // TODO: 'UPDATE' fails on HTTP BAD REQUEST
        // TODO: 'UPDATE' fails on HTTP FORBIDDEN
        // TODO: 'UPDATE' fails on HTTP NOT FOUND
        // TODO: 'UPDATE' fails on HTTP UNAUTHORIZED

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
            assertThatThrownBy(() -> client.createTag(createRequest))
                    .hasMessage("Missing parameter(s): param1")
                    .extracting(ex -> (WpBadRequestException) ex)
                    .extracting(WpBadRequestException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_missing_callback_param");
                        assertThat(error.getMessage()).isEqualTo("Missing parameter(s): param1");
                        assertThat(error.getData()).contains(entry("status", 400));
                    });
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
            assertThatThrownBy(() -> client.createTag(createRequest))
                    .hasMessage("Sorry, you are not allowed to do that.")
                    .extracting(ex -> (WpForbiddenException) ex)
                    .extracting(WpForbiddenException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_forbidden");
                        assertThat(error.getMessage()).isEqualTo("Sorry, you are not allowed to do that.");
                        assertThat(error.getData()).containsExactly(entry("status", 403));
                    });
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
            assertThatThrownBy(() -> client.createTag(createRequest))
                    .hasMessage("You are not currently logged in.")
                    .extracting(ex -> (WpUnauthorizedException) ex)
                    .extracting(WpUnauthorizedException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_not_logged_in");
                        assertThat(error.getMessage()).isEqualTo("You are not currently logged in.");
                        assertThat(error.getData()).containsExactly(entry("status", 401));
                    });
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
            assertThatThrownBy(() -> client.deleteTag(1005L))
                    .hasMessage("Sorry, you are not allowed to do that.")
                    .extracting(ex -> (WpForbiddenException) ex)
                    .extracting(WpForbiddenException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_forbidden");
                        assertThat(error.getMessage()).isEqualTo("Sorry, you are not allowed to do that.");
                        assertThat(error.getData()).containsExactly(entry("status", 403));
                    });
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void deleteFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/delete.failure.not-found.json");

            // WHEN/THEN
            assertThatThrownBy(() -> client.deleteTag(1005L))
                    .hasMessage("Term does not exist.")
                    .extracting(ex -> (WpNotFoundException) ex)
                    .extracting(WpNotFoundException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_term_invalid");
                        assertThat(error.getMessage()).isEqualTo("Term does not exist.");
                        assertThat(error.getData()).containsExactly(entry("status", 404));
                    });
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
            assertThatThrownBy(() -> client.deleteTag(1005L))
                    .hasMessage("You are not currently logged in.")
                    .extracting(ex -> (WpUnauthorizedException) ex)
                    .extracting(WpUnauthorizedException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_not_logged_in");
                        assertThat(error.getMessage()).isEqualTo("You are not currently logged in.");
                        assertThat(error.getData()).containsExactly(entry("status", 401));
                    });
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
            assertThatThrownBy(() -> client.getTag(1005L, null))
                    .hasMessage("Sorry, you are not allowed to do that.")
                    .extracting(ex -> (WpForbiddenException) ex)
                    .extracting(WpForbiddenException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_forbidden");
                        assertThat(error.getMessage()).isEqualTo("Sorry, you are not allowed to do that.");
                        assertThat(error.getData()).containsExactly(entry("status", 403));
                    });
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void getFailsOnNotFound() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.failure.not-found.json");

            // WHEN/THEN
            assertThatThrownBy(() -> client.getTag(1005L, null))
                    .hasMessage("Term does not exist.")
                    .extracting(ex -> (WpNotFoundException) ex)
                    .extracting(WpNotFoundException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_term_invalid");
                        assertThat(error.getMessage()).isEqualTo("Term does not exist.");
                        assertThat(error.getData()).containsExactly(entry("status", 404));
                    });
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
            assertThatThrownBy(() -> client.getTag(1005L, null))
                    .hasMessage("You are not currently logged in.")
                    .extracting(ex -> (WpUnauthorizedException) ex)
                    .extracting(WpUnauthorizedException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_not_logged_in");
                        assertThat(error.getMessage()).isEqualTo("You are not currently logged in.");
                        assertThat(error.getData()).containsExactly(entry("status", 401));
                    });
        }

        @DisplayName("'GET' works (with context)")
        @Test
        void getWorksWiThContext() {

            // GIVEN
            givenExpectationFromFile("basic-auth/tag/get.success.with-context.json");

            final Long tagId = 4L;

            // WHEN
            final WpTag tag = client.getTag(tagId, WpContext.EDIT);

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
        void getWorksWiThNoContext() {

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
            assertThatThrownBy(() -> client.listTags(WpPagingQuery.of(1, 10), null))
                    .hasMessage("Sorry, you are not allowed to do that.")
                    .extracting(ex -> (WpForbiddenException) ex)
                    .extracting(WpForbiddenException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_forbidden");
                        assertThat(error.getMessage()).isEqualTo("Sorry, you are not allowed to do that.");
                        assertThat(error.getData()).containsExactly(entry("status", 403));
                    });
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
            assertThatThrownBy(() -> client.listTags(WpPagingQuery.of(1, 10), null))
                    .hasMessage("You are not currently logged in.")
                    .extracting(ex -> (WpUnauthorizedException) ex)
                    .extracting(WpUnauthorizedException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_not_logged_in");
                        assertThat(error.getMessage()).isEqualTo("You are not currently logged in.");
                        assertThat(error.getData()).containsExactly(entry("status", 401));
                    });
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
