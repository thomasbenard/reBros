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

    public Node buildNode() {
        if (isLeaf())
            return buildLeafNode();
        if (isObject()) {
            return buildObjectNode();
        }
        return buildArrayNode();
    }

    private Node buildArrayNode() {
        ArrayNode arrayNode = arrayNode();
        elements().forEach(element -> arrayNode.addElement(element.buildNode()));
        return arrayNode;
    }

    private Node buildObjectNode() {
        ObjectNode objectNode = objectNode();
        Map<String, Content> children = children();
        for (String member : members()) {
            Content child = children.get(member);
            objectNode.addField(member, child.buildNode());
        }
        return objectNode;
    }

    private Node buildLeafNode() {
        return leafNode(pattern);
    }

    private List<Content> elements() {
        List<Content> elements = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(pattern);
        for (int i = 0; i < jsonArray.length(); i++) {
            elements.add(new JsonContent(jsonArray.get(i).toString()));
        }
        return elements;
    }

    private Map<String, Content> children() {
        JSONObject jsonObject = new JSONObject(pattern);
        Map<String, Content> children = new HashMap<>();
        for (String member : members()) {
            children.put(member, new JsonContent(jsonObject.get(member).toString()));
        }
        return children;
    }

    private Set<String> members() {
        return new JSONObject(pattern).keySet();
    }

}
