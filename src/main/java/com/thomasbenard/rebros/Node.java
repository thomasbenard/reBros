package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.thomasbenard.rebros.Match.branchMatch;
import static com.thomasbenard.rebros.Match.fieldMatch;

class Node {
    private final String pattern;

    Node(String pattern) {
        this.pattern = pattern;
    }

    private String pattern() {
        return pattern;
    }

    private boolean isObject() {
        try {
            new JSONObject(pattern());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isArray() {
        try {
            new JSONArray(pattern());
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
            return List.of(fieldMatch(pattern()));
        if (isObject()) {
            JSONObject jsonObject = new JSONObject(pattern());
            Match complexMatch = branchMatch();
            for (String member : jsonObject.keySet()) {
                Node child = new Node(jsonObject.get(member).toString());
                List<Match> matches = child.buildMatches();
                complexMatch = complexMatch.addField(member, matches.get(0));
            }
            return List.of(complexMatch);
        } else {
            List<Match> matches = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(pattern());
            for (int i = 0; i < jsonArray.length(); i++) {
                Node element = new Node(jsonArray.get(i).toString());
                matches.addAll(element.buildMatches());
            }
            return matches;
        }
    }
}
