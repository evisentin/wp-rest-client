package com.ev.wordpress.client.test.support;

import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.assertions.WordPressAssertions;
import com.ev.wordpress.client.domain.dto.WpCategory;
import com.ev.wordpress.client.domain.dto.enums.WpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.ev.wordpress.client.domain.dto.enums.WpTaxonomy.CATEGORY;

public abstract class AbstractJwtAuthenticationWpRestClientContractTest extends AbstractMockServerTest {
    private WpRestClient client;

    @BeforeEach
    void setUp() {
        this.client = client();
    }

    protected abstract WpBaseRestClient client();

    @DisplayName("'CATEGORY' Operations")
    @Nested
    class CategoryTests {

        private static final String CATEGORY_NAME = "my category";
        private static final String CATEGORY_DESCRIPTION = "my category description";
        private static final String CATEGORY_SLUG = "my-category";

        @DisplayName("'GET' works with context")
        @Test
        void getWorksWithContext() {

            // GIVEN
            givenExpectationFromFile("jwt-auth/category/get.success.with-context.json");

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
    }
}
