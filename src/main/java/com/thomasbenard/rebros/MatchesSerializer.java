package com.thomasbenard.rebros;

import java.util.List;
import java.util.Map;

public interface MatchesSerializer {
    String serialize(Map<String, List<Node>> elements);
}
