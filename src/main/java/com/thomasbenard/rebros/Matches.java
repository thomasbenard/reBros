package com.thomasbenard.rebros;

import java.util.*;

public class Matches {

    private final Map<String, List<Node>> elements;

    private Matches(Map<String, List<Node>> elements) {
        this.elements = elements;
    }

    public static Matches emptyResult() {
        return new Matches(new HashMap<>());
    }

    public Matches put(String identifier, String value) {
        return put(identifier, Node.leafNode(value));
    }

    public Matches put(String identifier, Node node) {
        if (!elements.containsKey(identifier))
            elements.put(identifier, new LinkedList<>());
        elements.get(identifier).add(node);
        return this;
    }

    public String serialize(MatchesSerializer serializer) {
        return serializer.serialize(elements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matches matches = (Matches) o;
        return Objects.equals(elements, matches.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
