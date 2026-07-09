package io.github.evisentin.wordpress.rest.client.domain.model.query;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSearchItemSubType;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSearchItemType;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Set;

/**
 * Represents a query object for searching WordPress resources.
 *
 * <p>This class encapsulates the filtering options supported by the
 * WordPress search endpoint.</p>
 *
 * <p>Instances are immutable and should be created using the
 * {@link Builder}.</p>
 */
@Getter
@Builder(setterPrefix = "with")
public class WpSearchQuery {

    /**
     * Scope under which the request is made. Defaults to {@link WpContext#VIEW}.
     */
    @Builder.Default
    private final WpContext context = WpContext.VIEW;

    /**
     * Search term used to match resources.
     */
    private final String search;

    /**
     * Set of resource IDs to exclude from the results.
     */
    @Singular
    private final Set<Long> excludeIds;

    /**
     * Set of resource IDs to include in the results.
     */
    @Singular
    private final Set<Long> includeIds;

    /**
     * Restricts the search to a specific resource type.
     */
    private final WpSearchItemType type;

    /**
     * Restricts the search to a specific subtype within the selected {@linkplain #type resource type}, such as a custom
     * post type or taxonomy.
     */
    private final WpSearchItemSubType subType;
}
