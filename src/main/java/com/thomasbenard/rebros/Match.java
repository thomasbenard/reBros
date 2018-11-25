package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.List;

public abstract class Match {

    static LeafMatch leafMatch(@NotNull String value) {
        return new LeafMatch(value);
    }

    public static ObjectMatch objectMatch() {
        return new ObjectMatch();
    }

    static ArrayMatch arrayMatch() {
        return new ArrayMatch();
    }

    @NotNull
    abstract List<Match> findChildrenMatching(String key);

    protected List<Match> elements() {
        return List.of(this);
    }
}
