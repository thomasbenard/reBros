package com.thomasbenard.rebros;

import java.util.*;

public class Result {

    private final Map<String, List<Match>> elements = new HashMap<>();

    public void put(String identifier, String value) {
        put(identifier, new Match(identifier, value));
    }

    public void put(String identifier, Match match) {
        if (!elements.containsKey(identifier))
            elements.put(identifier, new LinkedList<>());
        elements.get(identifier).add(match);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(elements, result.elements);
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
