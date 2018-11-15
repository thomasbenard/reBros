package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JsonContent implements Content {
    private final JSONObject rootObject;

    public JsonContent(@NotNull String inputData) {
        rootObject = new JSONObject(inputData);
    }

    public List<Match> getAllMatches(@NotNull String key) {
        List<Match> matches = new ArrayList<>();
        List<String> rawMatches = findObjectMatchingKey(this.rootObject, key);
        rawMatches.forEach(match -> matches.add(buildMatch(key, match)));
        return matches;
    }

    private @NotNull Match buildMatch(String key, String match) {
        if (!isLeafMatch(match))
            return new Match(key, match);
        JSONObject jsonObject = new JSONObject(match);
        Match complexMatch = new Match(key);
        for (String member : jsonObject.keySet())
            complexMatch = complexMatch.addField(member, jsonObject.get(member).toString());
        return complexMatch;
    }

    private boolean isLeafMatch(String match) {
        try {
            new JSONObject(match);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private @NotNull List<String> findObjectMatchingKey(JSONObject jsonObject, String key) {
        for (String member : jsonObject.keySet()) {
            if (member.equals(key))
                return List.of(jsonObject.get(key).toString());
            Optional<JSONObject> child = Optional.ofNullable(jsonObject.optJSONObject(member));
            if (child.isPresent())
                return findObjectMatchingKey(child.get(), key);
            Optional<JSONArray> childArray = Optional.ofNullable(jsonObject.optJSONArray(member));
            if (childArray.isPresent()) {
                int numberOfElements = childArray.get().length();
                List<String> matches = new ArrayList<>();
                for (int i = 0; i < numberOfElements; i++) {
                    matches.addAll(findObjectMatchingKey(childArray.get().getJSONObject(i), key));
                }
                return matches;
            }
        }
        return Collections.emptyList();
    }
}
