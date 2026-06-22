package io.github.evisentin.wordpress.rest.client.domain.model.query;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WpPaginationQueryTest implements WithAssertions {

    @Test
    @DisplayName("constructor should reject invalid page number")
    void constructorShouldRejectInvalidPageNumber() {
        assertThatThrownBy(() -> new WpPaginationQuery(0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page number must be greater than 0");
    }

    @Test
    @DisplayName("constructor should reject invalid page size")
    void constructorShouldRejectInvalidPageSize() {
        assertThatThrownBy(() -> new WpPaginationQuery(1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be greater than 0");
    }

    @Test
    @DisplayName("should create instance via constructor")
    void shouldCreateInstanceViaConstructor() {
        WpPaginationQuery query = new WpPaginationQuery(3, 50);

        assertThat(query.pageNumber()).isEqualTo(3);
        assertThat(query.pageSize()).isEqualTo(50);
    }

    @Test
    @DisplayName("should keep provided values when arguments are valid")
    void shouldKeepProvidedValuesWhenArgumentsAreValid() {
        WpPaginationQuery query = new WpPaginationQuery(2, 25);

        assertThat(query.pageNumber()).isEqualTo(2);
        assertThat(query.pageSize()).isEqualTo(25);
    }

    @Test
    @DisplayName("should reject page number less than one")
    void shouldRejectPageNumberLessThanOne() {
        assertThatThrownBy(() -> new WpPaginationQuery(0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page number must be greater than 0");
    }

    @Test
    @DisplayName("should reject page size less than one")
    void shouldRejectPageSizeLessThanOne() {
        assertThatThrownBy(() -> new WpPaginationQuery(1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be greater than 0");
    }
}
