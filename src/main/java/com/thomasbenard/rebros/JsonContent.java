package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static com.thomasbenard.rebros.Node.*;

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

    public Node buildMatch() {
        if (isLeaf())
            return buildLeafMatch();
        if (isObject()) {
            return buildComplexMatch();
        }
        return buildArrayMatch();
    }

    private Node buildArrayMatch() {
        ArrayNode arrayMatch = arrayNode();
        elements().forEach(element -> arrayMatch.addElement(element.buildMatch()));
        return arrayMatch;
    }

    private Node buildComplexMatch() {
        ObjectNode objectMatch = objectNode();
        Map<String, JsonContent> children = children();
        for (String member : members()) {
            JsonContent child = children.get(member);
            Node matches = child.buildMatch();
            objectMatch.addField(member, matches);
        }
        return objectMatch;
    }

    private Node buildLeafMatch() {
        return leafNode(pattern);
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
