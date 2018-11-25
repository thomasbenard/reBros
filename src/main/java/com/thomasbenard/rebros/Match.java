package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.*;

public class Match {
    @NotNull
    private final Map<String, Match> children;

    Match() {
        children = new HashMap<>();
    }

    static Match leafMatch(@NotNull String value) {
        return new LeafMatch(value);
    }

    public static Match objectMatch() {
        return new Match();
    }

    static ArrayMatch arrayMatch() {
        return new ArrayMatch();
    }

    @NotNull List<Match> findChildrenMatching(String key) {
        List<Match> matches = new ArrayList<>();
        for (String member : children.keySet()) {
            Match child = children.get(member);
            if (member.equals(key))
                matches.addAll(child.elements());
            else
                matches.addAll(child.findChildrenMatching(key));
        }
        return matches;
    }

    protected List<Match> elements() {
        return List.of(this);
    }

    void addChild(String memberName, Match match) {
        children.put(memberName, match);
    }

    public Match addField(String name, String value) {
        return addField(name, leafMatch(value));
    }

    @Override
    public String toString() {
        return children.toString();
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
        return Objects.equals(children, match.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children);
    }
}
