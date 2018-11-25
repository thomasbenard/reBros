package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.*;

public class ObjectMatch extends Match {
    @NotNull
    private final Map<String, Match> children;

    ObjectMatch() {
        children = new HashMap<>();
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

    public ObjectMatch addField(String name, String value) {
        return addField(name, leafMatch(value));
    }

    public ObjectMatch addField(String name, Match value) {
        children.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return children.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectMatch that = (ObjectMatch) o;
        return Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children);
    }
}
