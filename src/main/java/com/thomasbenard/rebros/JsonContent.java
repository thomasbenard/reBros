package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static com.thomasbenard.rebros.Match.*;

public class JsonContent implements Content {
    private final String pattern;

    public JsonContent(String pattern) {
        this.pattern = pattern;
    }

    private boolean isObject() {
        try {
            new JSONObject(pattern);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isArray() {
        try {
            new JSONArray(pattern);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isLeaf() {
        return !isObject() && !isArray();
    }

    public Match buildMatch() {
        if (isLeaf())
            return buildLeafMatch();
        if (isObject()) {
            return buildComplexMatch();
        }
        return buildArrayMatch();
    }

    private Match buildArrayMatch() {
        ArrayMatch arrayMatch = arrayMatch();
        elements().forEach(element -> arrayMatch.addElement(element.buildMatch()));
        return arrayMatch;
    }

    private Match buildComplexMatch() {
        Match objectMatch = objectMatch();
        Map<String, JsonContent> children = children();
        for (String member : members()) {
            JsonContent child = children.get(member);
            Match matches = child.buildMatch();
            objectMatch.addChild(member, matches);
        }
        return objectMatch;
    }

    private Match buildLeafMatch() {
        return leafMatch(pattern);
    }

    private List<JsonContent> elements() {
        List<JsonContent> elements = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(pattern);
        for (int i = 0; i < jsonArray.length(); i++) {
            elements.add(new JsonContent(jsonArray.get(i).toString()));
        }
        return elements;
    }

    private Map<String, JsonContent> children() {
        JSONObject jsonObject = new JSONObject(pattern);
        Map<String, JsonContent> children = new HashMap<>();
        for (String member : members()) {
            children.put(member, new JsonContent(jsonObject.get(member).toString()));
        }
        return children;
    }

    private Set<String> members() {
        return new JSONObject(pattern).keySet();
    }

}
