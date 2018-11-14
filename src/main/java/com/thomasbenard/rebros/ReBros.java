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
            if (fieldFound.isPresent())
                result.put(selectField, fieldFound.orElseThrow());
        }
        return result;
    }
}
