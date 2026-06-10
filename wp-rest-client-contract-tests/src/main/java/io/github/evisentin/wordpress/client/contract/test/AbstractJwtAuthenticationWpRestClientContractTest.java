package io.github.evisentin.wordpress.client.contract.test;

import io.github.evisentin.wordpress.client.domain.WpBaseRestClient;
import io.github.evisentin.wordpress.client.domain.WpRestClient;
import io.github.evisentin.wordpress.client.domain.assertions.WordPressAssertions;
import io.github.evisentin.wordpress.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy.CATEGORY;

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
    }
}
