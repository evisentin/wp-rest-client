package com.ev.wordpress.test.local.testcontainers.base;

import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.dto.requests.WpCategoryCreateUpdateRequest;
import com.ev.wordpress.client.domain.exception.WpBadRequestException;
import com.ev.wordpress.test.local.testcontainers.BaseWordPressIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

        adminClient = initAdminClient();
        standardUserClient = initStandardUserClient();
    }

    protected abstract WpRestClient initAdminClient();

    protected abstract WpRestClient initStandardUserClient();

    @DisplayName("Category APIs - Integration Tests")
    @Nested
    class CategoryTests {
        @Test
        void create__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            Long nonExistingParentId = 1000L;

            // WHEN/THEN

            final WpCategoryCreateUpdateRequest creationRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName("my category")
                                                 .withParentId(nonExistingParentId)
                                                 .build();

            assertThatThrownBy(() -> adminClient.createCategory(creationRequest))
                    .hasMessage("Parent term does not exist.")
                    .extracting(ex -> (WpBadRequestException) ex)
                    .extracting(WpBadRequestException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_term_invalid");
                        assertThat(error.getMessage()).isEqualTo("Parent term does not exist.");
                        assertThat(error.getData()).containsExactly(entry("status", 400));
                    });
        }
    }
}
