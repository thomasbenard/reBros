package com.thomasbenard.rebros;

import java.util.List;
import java.util.Map;

import static com.thomasbenard.rebros.Matches.emptyResult;

public class ReBros {
    private Content content;

    public ReBros(Content content) {
        this.content = content;
    }

    public Matches run(Request request) {
        Matches result = emptyResult();
        for (String selectField : request.selectedFields()) {
            List<Node> selectedFields = content.buildMatch().findChildrenMatching(selectField);
            Map<String, String> whereClauses = request.whereClauses();
            if (!whereClauses.isEmpty()) {
                for (Node node : selectedFields) {
                    if (node.contains(Node.leafNode(whereClauses.get("id"))))
                        result.put(selectField, node);
                }
            } else
                selectedFields.forEach(match -> result.put(selectField, match));
        }
        return result;
    }
}
