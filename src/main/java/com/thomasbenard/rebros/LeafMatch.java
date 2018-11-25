package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class LeafMatch extends Match {
    @NotNull
    private final String value;

    LeafMatch(@NotNull String value) {
        super();
        this.value = value;
    }

    @Override
    @NotNull List<Match> findChildrenMatching(String key) {
        return List.of(this);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LeafMatch leafMatch = (LeafMatch) o;
        return Objects.equals(value, leafMatch.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
