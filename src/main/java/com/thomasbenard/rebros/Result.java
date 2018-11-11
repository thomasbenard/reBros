package com.thomasbenard.rebros;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class Result {

    private final Map<String, String> elements = new Hashtable<>();

    public void put(String identifier, String value) {
        elements.put(identifier, value);
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
