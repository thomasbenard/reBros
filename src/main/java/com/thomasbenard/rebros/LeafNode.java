package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

public class LeafNode extends Node {
    @NotNull
    private final String value;

    LeafNode(@NotNull String value) {
        super();
        this.value = value;
    }

    @Override
    @NotNull
    public List<Node> findChildrenMatching(String key) {
        return emptyList();
    }

    @Override
    public List<Node> elements() {
        return List.of(this);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeafNode leafMatch = (LeafNode) o;
        return Objects.equals(value, leafMatch.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
