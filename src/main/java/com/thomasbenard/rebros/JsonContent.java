package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonContent implements Content {
    private final JSONObject rootObject;

    public JsonContent(@NotNull String inputData) {
        rootObject = new JSONObject(inputData);
    }

    public List<Match> getAllMatches(@NotNull String key) {
        List<Match> matches = new ArrayList<>();
        List<Node> matchingNodes = findObjectMatchingKey(this.rootObject, key);
        matchingNodes.forEach(node -> matches.addAll(buildMatches(node)));
        return matches;
    }

    private List<Match> buildMatches(Node node) {
        return node.buildMatches();
    }

    private @NotNull List<Node> findObjectMatchingKey(JSONObject jsonObject, String key) {
        for (String member : jsonObject.keySet()) {
            if (member.equals(key))
                return List.of(new Node(jsonObject.get(key).toString()));
            JSONObject child = jsonObject.optJSONObject(member);
            if (child != null)
                return findObjectMatchingKey(child, key);
            JSONArray childArray = jsonObject.optJSONArray(member);
            if (childArray != null) {
                int numberOfElements = childArray.length();
                List<Node> nodes = new ArrayList<>();
                for (int i = 0; i < numberOfElements; i++) {
                    nodes.addAll(findObjectMatchingKey(childArray.getJSONObject(i), key));
                }
                return nodes;
            }
        }
        return Collections.emptyList();
    }
}
