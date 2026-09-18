package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.layout;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.layout.WpGroupBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("GroupBlockAdapter")
class GroupBlockAdapterTest {
    private final GroupBlockAdapter adapter = new GroupBlockAdapter();

    @Test
    @DisplayName("fromBlock retains wrapper and child positions while copying collections")
    void fromBlock__succeeds__when_groupContainsNestedBlocks() {
        var child = new WpBlock("vendor/child", Map.of(), List.of(), WpBlockSyntax.SELF_CLOSING);
        var content = new ArrayList<WpContentNode>(List.of(new WpHtmlFragment("<div>"), child, new WpHtmlFragment("</div>")));
        var attributes = new HashMap<String, Object>(Map.of("layout", Map.of("type", "flex")));
        var source = new WpBlock("core/group", attributes, content, WpBlockSyntax.PAIRED);
        var model = adapter.fromBlock(source);
        assertThat(model.content()).containsExactlyElementsOf(content).isNotSameAs(content);
        assertThat(model.content().get(1)).isSameAs(child);
        assertThat(model.attributes()).isEqualTo(attributes).isNotSameAs(attributes);
        content.clear();
        attributes.clear();
        assertThat(model.content()).hasSize(3);
        assertThat(model.attributes()).containsKey("layout");
    }

    @Test
    @DisplayName("toBlock rejects a null group model")
    void toBlock__fails__when_modelIsNull() {
        assertThatNullPointerException().isThrownBy(() -> adapter.toBlock(null)).withMessage("model");
    }

    @Test
    @DisplayName("toBlock creates paired saved content without regenerating the wrapper")
    void toBlock__succeeds__when_groupModelHasSavedWrapper() {
        var content = new ArrayList<WpContentNode>(List.of(new WpHtmlFragment("<section>custom</section>")));
        var attributes = new HashMap<String, Object>(Map.of("custom", true));
        var result = adapter.toBlock(new WpGroupBlock(content, attributes));
        assertThat(result).isEqualTo(new WpBlock("core/group", attributes, content, WpBlockSyntax.PAIRED));
        assertThat(result.attributes()).isNotSameAs(attributes);
        assertThat(result.content()).isNotSameAs(content);
        assertThatThrownBy(() -> result.content().clear()).isInstanceOf(UnsupportedOperationException.class);
    }
}
