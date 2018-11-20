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
        List<String> rawMatches = findObjectMatchingKey(this.rootObject, key);
        rawMatches.forEach(match -> matches.addAll(buildMatches(match)));
        return matches;
    }

    private List<Match> buildMatches(String match) {
        return new Node(match).buildMatches();
    }

    private @NotNull List<String> findObjectMatchingKey(JSONObject jsonObject, String key) {
        for (String member : jsonObject.keySet()) {
            if (member.equals(key))
                return List.of(jsonObject.get(key).toString());
            JSONObject child = jsonObject.optJSONObject(member);
            if (child != null)
                return findObjectMatchingKey(child, key);
            JSONArray childArray = jsonObject.optJSONArray(member);
            if (childArray != null) {
                int numberOfElements = childArray.length();
                List<String> matches = new ArrayList<>();
                for (int i = 0; i < numberOfElements; i++) {
                    matches.addAll(findObjectMatchingKey(childArray.getJSONObject(i), key));
                }
                return matches;
            }
        }
        return Collections.emptyList();
    }
}
