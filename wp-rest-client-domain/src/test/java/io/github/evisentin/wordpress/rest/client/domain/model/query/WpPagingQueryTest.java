package io.github.evisentin.wordpress.rest.client.domain.model.query;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WpPagingQueryTest implements WithAssertions {

    @Test
    @DisplayName("factory method should reject invalid page number")
    void factoryMethodShouldRejectInvalidPageNumber() {
        assertThatThrownBy(() -> WpPagingQuery.of(0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page number must be greater than 0");
    }

    @Test
    @DisplayName("factory method should reject invalid page size")
    void factoryMethodShouldRejectInvalidPageSize() {
        assertThatThrownBy(() -> WpPagingQuery.of(1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be greater than 0");
    }

    @Test
    @DisplayName("should create instance via factory method")
    void shouldCreateInstanceViaFactoryMethod() {
        WpPagingQuery query = WpPagingQuery.of(3, 50);

        assertThat(query.getPageNumber()).isEqualTo(3);
        assertThat(query.getPageSize()).isEqualTo(50);
    }

    @Test
    @DisplayName("should keep provided values when arguments are valid")
    void shouldKeepProvidedValuesWhenArgumentsAreValid() {
        WpPagingQuery query = new WpPagingQuery(2, 25);

        assertThat(query.getPageNumber()).isEqualTo(2);
        assertThat(query.getPageSize()).isEqualTo(25);
    }

    @Test
    @DisplayName("should reject page number less than one")
    void shouldRejectPageNumberLessThanOne() {
        assertThatThrownBy(() -> new WpPagingQuery(0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page number must be greater than 0");
    }

    @Test
    @DisplayName("should reject page size less than one")
    void shouldRejectPageSizeLessThanOne() {
        assertThatThrownBy(() -> new WpPagingQuery(1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be greater than 0");
    }
}
