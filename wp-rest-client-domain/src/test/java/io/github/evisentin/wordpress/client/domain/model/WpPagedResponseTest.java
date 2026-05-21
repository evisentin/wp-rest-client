package io.github.evisentin.wordpress.client.domain.model;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class WpPagedResponseTest implements WithAssertions {

    @Test
    @DisplayName("should keep provided values when arguments are valid")
    void shouldKeepProvidedValuesWhenArgumentsAreValid() {
        List<String> items = List.of("one", "two");

        WpPagedResponse<String> response = new WpPagedResponse<>(
                items,
                10,
                25,
                3,
                1
        );

        assertThat(response.getItems()).containsExactly("one", "two");
        assertThat(response.getItemsPerPage()).isEqualTo(10);
        assertThat(response.getTotalItems()).isEqualTo(25);
        assertThat(response.getTotalPages()).isEqualTo(3);
        assertThat(response.getPageNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("should not report next page when page number equals total pages")
    void shouldNotReportNextPageWhenPageNumberEqualsTotalPages() {
        WpPagedResponse<String> response = new WpPagedResponse<>(
                List.of("item"),
                10,
                30,
                3,
                3
        );

        assertThat(response.hasNextPage()).isFalse();
    }

    @Test
    @DisplayName("should reject itemsPerPage less than one")
    void shouldRejectItemsPerPageLessThanOne() {
        assertThatThrownBy(() -> new WpPagedResponse<>(
                List.of("item"),
                0,
                0,
                0,
                1
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("itemsPerPage must be >= 1");
    }

    @Test
    @DisplayName("should reject negative total items")
    void shouldRejectNegativeTotalItems() {
        assertThatThrownBy(() -> new WpPagedResponse<>(
                List.of("item"),
                10,
                -1,
                0,
                1
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("totalItems must be >= 0");
    }

    @Test
    @DisplayName("should reject negative total pages")
    void shouldRejectNegativeTotalPages() {
        assertThatThrownBy(() -> new WpPagedResponse<>(
                List.of("item"),
                10,
                0,
                -1,
                1
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("totalPages must be >= 0");
    }

    @Test
    @DisplayName("should reject page number less than one")
    void shouldRejectPageNumberLessThanOne() {
        assertThatThrownBy(() -> new WpPagedResponse<>(
                List.of("item"),
                10,
                0,
                0,
                0
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("pageNumber must be >= 1");
    }

    @Test
    @DisplayName("should replace null items with an empty list")
    void shouldReplaceNullItemsWithEmptyList() {
        WpPagedResponse<String> response = new WpPagedResponse<>(
                null,
                10,
                0,
                0,
                1
        );

        assertThat(response.getItems()).isEmpty();
        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("should report empty when items list is empty")
    void shouldReportEmptyWhenItemsListIsEmpty() {
        WpPagedResponse<String> response = new WpPagedResponse<>(
                List.of(),
                10,
                0,
                0,
                1
        );

        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("should report next page when page number is less than total pages")
    void shouldReportNextPageWhenPageNumberIsLessThanTotalPages() {
        WpPagedResponse<String> response = new WpPagedResponse<>(
                List.of("item"),
                10,
                30,
                3,
                2
        );

        assertThat(response.hasNextPage()).isTrue();
    }

    @Test
    @DisplayName("should report not empty when items list contains elements")
    void shouldReportNotEmptyWhenItemsListContainsElements() {
        WpPagedResponse<String> response = new WpPagedResponse<>(
                List.of("item"),
                10,
                1,
                1,
                1
        );

        assertThat(response.isEmpty()).isFalse();
    }
}
