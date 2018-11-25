package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface Node {

    static LeafNode leafNode(@NotNull String value) {
        return new LeafNode(value);
    }

    static ObjectNode objectNode() {
        return new ObjectNode();
    }

    static ArrayNode arrayNode() {
        return new ArrayNode();
    }

    @NotNull List<Node> findChildrenMatching(String key);

    List<Node> elements();

    boolean contains(Node node);
}
