package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.*;

public class Match {
    @NotNull
    private final Map<String, Match> children;
    @NotNull
    private final List<Match> elements;

    private Match(@NotNull String value) {
        children = new HashMap<>();
        elements = new ArrayList<>();
    }

    Match() {
        children = new HashMap<>();
        elements = new ArrayList<>();
    }

    static Match leafMatch(@NotNull String value) {
        return new LeafMatch(value);
    }

    public static Match objectMatch() {
        return new Match();
    }

    static Match arrayMatch() {
        return new Match();
    }

    private boolean isObject() {
        return !children.isEmpty();
    }

    private boolean isArray() {
        return !elements.isEmpty();
    }

    @NotNull List<Match> findChildrenMatching(String key) {
        List<Match> matches = new ArrayList<>();
        if (isObject()) {
            for (String member : children.keySet()) {
                Match child = children.get(member);
                if (member.equals(key)) {
                    if (child.isArray())
                        matches.addAll(child.elements);
                    else
                        matches.add(child);
                } else if (child.isObject() || child.isArray())
                    matches.addAll(child.findChildrenMatching(key));
            }
        }
        if (isArray()) {
            elements.forEach(element -> matches.addAll(element.findChildrenMatching(key)));
        }
        return matches;
    }

    void addChild(String memberName, Match match) {
        children.put(memberName, match);
    }

    void addElement(Match element) {
        elements.add(element);
    }

    public Match addField(String name, String value) {
        return addField(name, leafMatch(value));
    }

    @Override
    public String toString() {
        if (!children.isEmpty())
            return children.toString();
        return elements.toString();
    }

    public Match addField(String name, Match value) {
        addChild(name, value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(children, match.children) &&
                Objects.equals(elements, match.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children, elements);
    }
}
