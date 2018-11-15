package com.thomasbenard.rebros;

import java.util.List;

public class ReBros {
    private Content content;

    public ReBros(Content content) {
        this.content = content;
    }

    public Result run(Request request) {
        Result result = new Result();
        for (String selectField : request.selectedFields()) {
            List<Match> matches = content.getAllMatches(selectField);
            matches.forEach(match -> result.put(selectField, match));
        }
        return result;
    }
}
