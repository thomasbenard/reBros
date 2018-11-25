package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.List;

public class JsonContent implements Content {
    private final Node rootNode;

    public JsonContent(@NotNull String inputData) {
        rootNode = new Node(inputData);
    }

    public List<Match> getAllMatches(@NotNull String key) {
        return rootNode.buildMatch().findChildrenMatching(key);
    }

}
