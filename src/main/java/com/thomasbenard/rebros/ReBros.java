package com.thomasbenard.rebros;

import java.util.Optional;

public class ReBros {
    private Content content;

    public ReBros(Content content) {
        this.content = content;
    }

    public Result run(Request request) {
        Result result = new Result();
        for (String selectField : request.selectedFields()) {
            Optional<Match> fieldFound = content.get(selectField);
            fieldFound.ifPresent(match -> result.put(selectField, match));
        }
        return result;
    }
}
