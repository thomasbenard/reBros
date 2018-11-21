package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class JsonContent implements Content {
    private final Node rootNode;

    public JsonContent(@NotNull String inputData) {
        rootNode = new Node(inputData);
    }

    public List<Match> getAllMatches(@NotNull String key) {
        List<Match> matches = new ArrayList<>();
        List<Node> matchingNodes = rootNode.findObjectMatchingKey(key);
        matchingNodes.forEach(node -> matches.addAll(node.buildMatches()));
        return matches;
    }

}
