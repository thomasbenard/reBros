package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.*;

import static com.thomasbenard.rebros.Match.branchMatch;
import static com.thomasbenard.rebros.Match.fieldMatch;

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

    List<Match> buildMatches() {
        if (isLeaf())
            return List.of(buildLeafMatch());
        if (isObject()) {
            return List.of(buildComplexMatch());
        }
        List<Match> matches = new ArrayList<>();
        elements().forEach(node -> matches.addAll(node.buildMatches()));
        return matches;
    }

    private Match buildLeafMatch() {
        return fieldMatch(pattern);
    }

    private Match buildComplexMatch() {
        Match complexMatch = branchMatch();
        Map<String, Node> children = children();
        for (String member : members()) {
            Node child = children.get(member);
            List<Match> matches = child.buildMatches();
            complexMatch = complexMatch.addField(member, matches.get(0));
        }
        return complexMatch;
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

    private Node get(String member) {
        return children().get(member);
    }

    @NotNull List<Node> findChildrenMatching(String key) {
        for (String childName : members()) {
            Node child = get(childName);
            if (childName.equals(key))
                return List.of(child);
            if (child.isObject())
                return child.findChildrenMatching(key);
            if (child.isArray()) {
                List<Node> nodes = new ArrayList<>();
                child.elements().forEach(element -> nodes.addAll(element.findChildrenMatching(key)));
                return nodes;
            }
        }
        return Collections.emptyList();
    }
}
