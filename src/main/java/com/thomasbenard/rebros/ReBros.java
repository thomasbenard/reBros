package com.thomasbenard.rebros;

public class ReBros {
    private Content content;

    public ReBros(Content content) {
        this.content = content;
    }

    public Matches run(Request request) {
        return request.apply(content.buildMatch());
    }

}
