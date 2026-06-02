package io.github.evisentin.wordpress.test.integration.base;

import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.assertions.WordPressAssertions;
import io.github.evisentin.wordpress.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.test.integration.BaseWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.WpJwtAuthRestClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy.CATEGORY;

@Slf4j
public abstract class JwtAuthWordPressIntegrationTest extends BaseWordPressIntegrationTest {
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
            wpInstallAndActivateWpRestApiAuthenticationPlugin(true);
            wpCleanDefaultData();
            log.info("installWordpress ({}): initialized", getWordPressVersion());
        }

        log.info("installWordpress ({}): END", getWordPressVersion());

        adminClient = clientFactory().create(
                getHttpsBaseUrl(),
                getHttpsBaseUrl() + "/wp-json/api/v1/token",
                WP_ADMIN_USER_NAME,
                WP_ADMIN_PASSWORD // JWT wants this, not the adminApplicationPassword
        );

        standardUserClient = clientFactory().create(
                getHttpsBaseUrl(),
                getHttpsBaseUrl() + "/wp-json/api/v1/token",
                WP_STANDARD_USER_NAME,
                WP_STANDARD_USER_PASSWORD
        );
    }

    protected abstract WpJwtAuthRestClientFactory clientFactory();

    private Long givenCategoryExists(final String name, final String description, final String slug) {
        return wpCreateCategory(name, description, slug);
    }

    @DisplayName("Category APIs - Integration Tests")
    @Nested
    class CategoryTests {

        private static final String CATEGORY_1_NAME = "Category #1";
        private static final String CATEGORY_1_DESCRIPTION = "My first category";
        private static final String CATEGORY_1_SLUG = "category-1";

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
    }
}
