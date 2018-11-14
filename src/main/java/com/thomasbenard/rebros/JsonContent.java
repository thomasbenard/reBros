package com.thomasbenard.rebros;

import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class JsonContent implements Content {
    private final JSONObject rootObject;

    public JsonContent(@NotNull String inputData) {
        rootObject = new JSONObject(inputData);
    }

    public Optional<Match> get(@NotNull String key) {
        Optional<String> match = findObjectMatchingKey(this.rootObject, key);
        return match.flatMap(s -> buildMatch(key, s));
    }

    private Optional<Match> buildMatch(String key, String match) {
        if (!isLeafMatch(match))
            return Optional.of(new Match(key, match));
        JSONObject jsonObject = new JSONObject(match);
        Match complexMatch = new Match(key);
        for (String member : jsonObject.keySet())
            complexMatch = complexMatch.addField(member, jsonObject.get(member).toString());
        return Optional.of(complexMatch);
    }

    private boolean isLeafMatch(String match) {
        try {
            new JSONObject(match);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Optional<String> findObjectMatchingKey(JSONObject jsonObject, String key) {
        for (String member : jsonObject.keySet()) {
            if (member.equals(key))
                return Optional.of(jsonObject.get(key).toString());
            Optional<JSONObject> child = Optional.ofNullable(jsonObject.optJSONObject(member));
            if (child.isPresent())
                return findObjectMatchingKey(child.get(), key);
        }
        return Optional.empty();
    }
}
