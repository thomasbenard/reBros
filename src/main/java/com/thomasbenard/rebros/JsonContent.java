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
        return buildMatch(key, findObjectMatchKey(this.rootObject, key));
    }

    private Optional<Match> buildMatch(String key, Optional<String> match) {
        if (!match.isPresent())
            return Optional.empty();
        if (!isLeafMatch(match.get()))
            return Optional.of(new Match(key, match.get()));
        JSONObject jsonObject = new JSONObject(match.get());
        Match todomatch = new Match(key);
        for (String member : jsonObject.keySet()) {
            todomatch = todomatch.addField(member, jsonObject.get(member).toString());
        }
        return Optional.of(todomatch);
    }

    private boolean isLeafMatch(String match) {
        try {
            new JSONObject(match);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Optional<String> findObjectMatchKey(JSONObject jsonObject, String key) {
        for (String member : jsonObject.keySet()) {
            if (member.equals(key))
                return Optional.ofNullable(jsonObject.get(key).toString());
            JSONObject child = jsonObject.optJSONObject(member);
            if (child != null)
                return findObjectMatchKey(child, key);
        }
        return Optional.empty();
    }
}
