package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.*;

public class Match {
    @NotNull
    private final String value;
    @NotNull
    private final Map<String, List<Match>> children;

    static Match fieldMatch(String fieldValue) {
        return new Match(fieldValue);
    }

    public static Match branchMatch() {
        return new Match();
    }

    private Match(String value) {
        this.value = value;
        children = new HashMap<>();
    }

    private Match() {
        this.value = "";
        children = new HashMap<>();
    }

    private Match(HashMap<String, List<Match>> children) {
        this.value = "";
        this.children = children;
    }

    public Match addField(String fieldName, String fieldValue) {
        return addField(fieldName, fieldMatch(fieldValue));
    }

    public Match addField(String fieldName, Match fieldValue) {
        if (!children.containsKey(fieldName))
            children.put(fieldName, new ArrayList<>());
        HashMap<String, List<Match>> newChildren = new HashMap<>(children);
        newChildren.get(fieldName).add(fieldValue);
        return new Match(newChildren);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(value, match.value) &&
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
        return Objects.hash(value, children);
    }

    @Override
    public String toString() {
        if(!value.isEmpty())
            return value;
        return children.toString();
    }

}
