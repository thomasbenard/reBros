package com.thomasbenard.rebros;

import java.util.*;

import static com.thomasbenard.rebros.Matches.emptyResult;

public class Request {
    private List<String> selectedFields = new ArrayList<>();
    private Map<String, Node> whereClauses = new HashMap<>();

    public void select(String... field) {
        selectedFields.addAll(Arrays.asList(field));
    }

    public void where(String memberName, String value) {
        whereClauses.put(memberName, Node.leafNode(value));
    }

    Matches apply(Node root) {
        Matches result = emptyResult();
        for (String selectField : selectedFields) {
            List<Node> selectedFields = root.findChildrenMatching(selectField);
            List<Node> matches = new ArrayList<>(selectedFields);
            Map<String, Node> whereClauses = this.whereClauses;
            if (!whereClauses.isEmpty()) {
                for (Node node : selectedFields) {
                    if (!node.contains(whereClauses.get("id")))
                        matches.remove(node);
                }
            }
            matches.forEach(match -> result.put(selectField, match));
        }
        return result;
    }
}
