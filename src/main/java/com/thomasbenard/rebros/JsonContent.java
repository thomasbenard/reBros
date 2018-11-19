package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.thomasbenard.rebros.Match.fieldMatch;

public class JsonContent implements Content {
    private final JSONObject rootObject;

    public JsonContent(@NotNull String inputData) {
        rootObject = new JSONObject(inputData);
    }

    public List<Match> getAllMatches(@NotNull String key) {
        List<Match> matches = new ArrayList<>();
        List<String> rawMatches = findObjectMatchingKey(this.rootObject, key);
        rawMatches.forEach(match -> matches.addAll(buildMatches(key, match)));
        return matches;
    }

    private List<Match> buildMatches(String key, String match) {
        if (!isJsonObject(match) && !isJsonArray(match))
            return List.of(fieldMatch(match));
        if (isJsonObject(match)) {
            JSONObject jsonObject = new JSONObject(match);
            Match complexMatch = new Match();
            for (String member : jsonObject.keySet()) {
                complexMatch = complexMatch.addField(member, jsonObject.get(member).toString());
            }
            return List.of(complexMatch);
        } else {
            List<Match> matches = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(match);
            for (int i = 0; i < jsonArray.length(); i++) {
                //element does not contain key ! => bug ! TODO remove key from Match
                matches.addAll(buildMatches(key, jsonArray.get(i).toString()));
            }
            return matches;
        }
    }

    private boolean isJsonObject(String match) {
        try {
            new JSONObject(match);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isJsonArray(String match) {
        try {
            new JSONArray(match);
            return true;
        } catch (Exception e) {
            return false;
        }
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
