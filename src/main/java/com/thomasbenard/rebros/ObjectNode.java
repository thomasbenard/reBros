package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.*;

public class ObjectNode extends Node {
    @NotNull
    private final Map<String, Node> children;

    ObjectNode() {
        children = new HashMap<>();
    }

    @NotNull List<Node> findChildrenMatching(String key) {
        List<Node> nodes = new ArrayList<>();
        children.forEach(
                (member, child) -> nodes.addAll(member.equals(key) ? child.elements() : child.findChildrenMatching(key)));
        return nodes;
    }

    public ObjectNode addField(String name, String value) {
        return addField(name, leafNode(value));
    }

    public ObjectNode addField(String name, Node value) {
        children.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return children.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectNode that = (ObjectNode) o;
        return Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children);
    }
}
