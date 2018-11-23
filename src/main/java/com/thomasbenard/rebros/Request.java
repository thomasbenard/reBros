package com.thomasbenard.rebros;

import java.util.*;

public class Request {
    private List<String> selectedFields = new ArrayList<>();
    private Map<String, String> whereClauses = new HashMap<>();

    public void select(String... field) {
        selectedFields.addAll(Arrays.asList(field));
    }

    List<String> selectedFields() {
        return selectedFields;
    }

    public void where(String memberName, String value) {
        whereClauses.put(memberName, value);
    }

    boolean isWhereCalled() {
        return !whereClauses.isEmpty();
    }
}
