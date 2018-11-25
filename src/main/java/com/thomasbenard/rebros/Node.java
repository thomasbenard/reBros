package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.List;

public abstract class Node {

    static LeafNode leafNode(@NotNull String value) {
        return new LeafNode(value);
    }

    public static ObjectNode objectNode() {
        return new ObjectNode();
    }

    static ArrayNode arrayNode() {
        return new ArrayNode();
    }

    @NotNull
    abstract List<Node> findChildrenMatching(String key);

    protected List<Node> elements() {
        return List.of(this);
    }
}
