package com.thomasbenard.rebros;

import java.util.*;
import java.util.stream.Collectors;

import static com.thomasbenard.rebros.Matches.emptyResult;
import static com.thomasbenard.rebros.Node.leafNode;

public class Request {
    private List<String> selectedFields = new ArrayList<>();
    private Map<String, Node> whereClauses = new HashMap<>();

    public void select(String... field) {
        selectedFields.addAll(Arrays.asList(field));
    }

    public void where(String memberName, String value) {
        whereClauses.put(memberName, leafNode(value));
    }

    Matches apply(Node root) {
        Matches result = emptyResult();
        for (String selectField : selectedFields) {
            List<Node> selectedFields = root.findChildrenMatching(selectField);
            List<Node> matches = filterWhereClauses(selectedFields);
            matches.forEach(match -> result.put(selectField, match));
        }
        return result;
    }

    private List<Node> filterWhereClauses(List<Node> selectedFields) {
        if (!whereClauses.isEmpty()) {
            return selectedFields.stream()
                    .filter(node -> node.contains(whereClauses.get("id")))
                    .collect(Collectors.toList());
        }
        return selectedFields;
    }
}
