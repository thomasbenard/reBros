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

    abstract List<Node> elements();

    boolean contains(String parameterName, Node node) {
        return equals(node) || findChildrenMatching(parameterName).contains(node);
    }
}
