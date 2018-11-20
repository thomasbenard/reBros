package com.thomasbenard.rebros;

import org.json.JSONArray;
import org.json.JSONObject;

class Node {
    private final String pattern;

    Node(String pattern) {
        this.pattern = pattern;
    }

    String pattern() {
        return pattern;
    }

    boolean isObject() {
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

    boolean isLeaf() {
        return !isObject() && !isArray();
    }
}
