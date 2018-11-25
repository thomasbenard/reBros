package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArrayNode implements Node {
    @NotNull
    private final List<Node> elements;

    ArrayNode() {
        elements = new ArrayList<>();
    }

    void addElement(Node element) {
        elements.add(element);
    }

    @Override
    @NotNull
    public List<Node> findChildrenMatching(String key) {
        List<Node> nodes = new ArrayList<>();
        elements.forEach(match -> nodes.addAll(match.findChildrenMatching(key)));
        return nodes;
    }

    @Override
    public List<Node> elements() {
        return elements;
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayNode that = (ArrayNode) o;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}
