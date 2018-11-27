package com.thomasbenard.rebros.infra;

import com.thomasbenard.rebros.MatchesSerializer;
import com.thomasbenard.rebros.Node;

import java.util.List;
import java.util.Map;

public class ToStringMatchesSerializer implements MatchesSerializer {
    @Override
    public String serialize(Map<String, List<Node>> elements) {
        return elements.toString();
    }
}
