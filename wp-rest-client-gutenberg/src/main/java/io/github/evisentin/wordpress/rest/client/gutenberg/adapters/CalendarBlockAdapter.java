package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCalendarBlock;

/**
 * Saved-content adapter for {@code core/calendar}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:calendar {"month":9,"year":2026} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CalendarBlockAdapter extends SavedBlockAdapter<WpCalendarBlock> {
    /**
     * Creates an adapter for {@code core/calendar}.
     */
    public CalendarBlockAdapter() {
        super("core/calendar", WpCalendarBlock.class, WpCalendarBlock::new);
    }
}
