package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Match {
    @NotNull
    private final String name;
    @NotNull
    private final String value;
    @NotNull
    private final List<@NotNull Match> children;

    Match(String name, String value) {
        this.name = name;
        this.value = value;
        children = new ArrayList<>();
    }

    public Match(String name) {
        this.name = name;
        this.value = "";
        children = new ArrayList<>();
    }

    private Match(String name, List<Match> children) {
        this.name = name;
        this.value = "";
        this.children = children;
    }

    public Match addField(String fieldName, String fieldValue) {
        List<@NotNull Match> newChildren = new ArrayList<>(children);
        newChildren.add(new Match(fieldName, fieldValue));
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
        for (Match child : children) {
            areChildrenEqual &= match.children.contains(child);
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
