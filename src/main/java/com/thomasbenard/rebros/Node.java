package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

class Node {
    private final String pattern;

    Node(String pattern) {
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

    Match buildMatch() {
        if (isLeaf())
            return buildLeafMatch();
        if (isObject()) {
            return buildComplexMatch();
        }
        return buildArrayMatch();
    }

    private Match buildArrayMatch() {
        Match arrayMatch = Match.buildArrayMatch();
        elements().forEach(element -> arrayMatch.addElement(element.buildMatch()));
        return arrayMatch;
    }

    private Match buildComplexMatch() {
        Match objectMatch = Match.buildObjectMatch();
        Map<String, Node> children = children();
        for (String member : members()) {
            Node child = children.get(member);
            Match matches = child.buildMatch();
            objectMatch.addChild(member, matches);
        }
        return objectMatch;
    }

    private Match buildLeafMatch() {
        return Match.buildLeaf(pattern);
    }

    private List<Node> elements() {
        List<Node> elements = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(pattern);
        for (int i = 0; i < jsonArray.length(); i++) {
            elements.add(new Node(jsonArray.get(i).toString()));
        }
        return elements;
    }

    private Map<String, Node> children() {
        JSONObject jsonObject = new JSONObject(pattern);
        Map<String, Node> children = new HashMap<>();
        for (String member : members()) {
            children.put(member, new Node(jsonObject.get(member).toString()));
        }
        return children;
    }

    private Set<String> members() {
        return new JSONObject(pattern).keySet();
    }

}
