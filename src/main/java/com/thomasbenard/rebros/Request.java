package com.thomasbenard.rebros;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.thomasbenard.rebros.Matches.emptyResult;
import static com.thomasbenard.rebros.Node.leafNode;

public class Request {
    private List<String> selectedFields = new ArrayList<>();
    private Map<String, Node> whereClauses = new HashMap<>();

    public Request select(String... field) {
        selectedFields.addAll(Arrays.asList(field));
        return this;
    }

    public Request where(String memberName, String value) {
        whereClauses.put(memberName, leafNode(value));
        return this;
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
        Stream<Node> stream = selectedFields.stream();
        for (String parameterName : whereClauses.keySet()) {
            Node whereClauseValue = whereClauses.get(parameterName);
            stream = stream.filter(node -> node.equals(whereClauseValue)
                    || node.findChildrenMatching(parameterName).contains(whereClauseValue));
        }
        return stream.collect(Collectors.toList());
    }
}
