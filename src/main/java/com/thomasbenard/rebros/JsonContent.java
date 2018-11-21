package com.thomasbenard.rebros;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonContent implements Content {
    private final Node rootNode;

    public JsonContent(@NotNull String inputData) {
        rootNode = new Node(inputData);
    }

    public List<Match> getAllMatches(@NotNull String key) {
        List<Match> matches = new ArrayList<>();
        List<Node> matchingNodes = findObjectMatchingKey(rootNode, key);
        matchingNodes.forEach(node -> matches.addAll(node.buildMatches()));
        return matches;
    }

    private @NotNull List<Node> findObjectMatchingKey(Node node, String key) {
        for (String member : node.members()) {
            Node childNode = node.get(member);
            if (member.equals(key))
                return List.of(childNode);
            if (childNode.isObject())
                return findObjectMatchingKey(childNode, key);
            if (childNode.isArray()) {
                List<Node> nodes = new ArrayList<>();
                childNode.elements().forEach(element -> nodes.addAll(findObjectMatchingKey(element, key)));
                return nodes;
            }
        }
        return Collections.emptyList();
    }

}
