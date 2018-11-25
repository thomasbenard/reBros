package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArrayMatch extends Match {
    @NotNull
    private final List<Match> elements;

    ArrayMatch() {
        elements = new ArrayList<>();
    }

    void addElement(Match element) {
        elements.add(element);
    }

    @Override
    @NotNull List<Match> findChildrenMatching(String key) {
        List<Match> matches = new ArrayList<>();
        elements.forEach(match -> matches.addAll(match.findChildrenMatching(key)));
        return matches;
    }

    @Override
    protected List<Match> elements() {
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
        ArrayMatch that = (ArrayMatch) o;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}
