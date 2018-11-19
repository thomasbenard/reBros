package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Match {
    @NotNull
    private final String name;
    @NotNull
    private final String value;
    @NotNull
    private final Map<String, @NotNull Match> children;

    static Match fieldMatch(String fieldName, String fieldValue) {
        return new Match(fieldName, fieldValue);
    }

    private Match(String name, String value) {
        this.name = name;
        this.value = value;
        children = new HashMap<>();
    }

    public Match() {
        this.name = "";
        this.value = "";
        children = new HashMap<>();
    }

    private Match(String name, HashMap<String, @NotNull Match> children) {
        this.name = name;
        this.value = "";
        this.children = children;
    }

    public Match addField(String fieldName, String fieldValue) {
        HashMap<String, Match> newChildren = new HashMap<>(children);
        newChildren.put(fieldName, fieldMatch(fieldName, fieldValue));
        return new Match(this.name, newChildren);
    }

    public Match addField(String fieldName, Match fieldValue) {
        HashMap<String, Match> newChildren = new HashMap<>(children);
        newChildren.put(fieldName, fieldValue);
        return new Match(this.name, newChildren);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(name, match.name) &&
                Objects.equals(value, match.value) &&
                areChildrenEqual(match);
    }

    private boolean areChildrenEqual(Match match) {
        boolean areChildrenEqual = children.size() == match.children.size();
        for (String field : children.keySet()) {
            areChildrenEqual &= children.get(field).equals(match.children.get(field));
        }
        return areChildrenEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, children);
    }

    @Override
    public String toString() {
        return "Match{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", children=" + children +
                '}';
    }

}
