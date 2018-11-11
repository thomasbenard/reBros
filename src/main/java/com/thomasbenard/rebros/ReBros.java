package com.thomasbenard.rebros;

public class ReBros {
    private Content content;

    public ReBros(Content content) {
        this.content = content;
    }

    public Result run(Request request) {
        Result result = new Result();
        for (String selectField : request.selectedFields()) {
            result.put(selectField, content.get(selectField));
        }
        return result;
    }
}
