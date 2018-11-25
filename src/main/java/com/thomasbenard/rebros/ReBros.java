package com.thomasbenard.rebros;

import java.util.List;

import static com.thomasbenard.rebros.Result.emptyResult;

public class ReBros {
    private Content content;

    public ReBros(Content content) {
        this.content = content;
    }

    public Result run(Request request) {
        Result result = emptyResult();
        for (String selectField : request.selectedFields()) {
            List<Match> matches = content.buildMatch().findChildrenMatching(selectField);
            if (request.isWhereCalled())
                matches.remove(matches.size() - 1);
            matches.forEach(match -> result.put(selectField, match));
        }
        return result;
    }
}
