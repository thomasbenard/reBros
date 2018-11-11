package com.thomasbenard.rebros;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private List<String> selectedFields = new ArrayList<>();

    public void select(String... identifier) {
        for (String field : identifier) {
            selectedFields.add(field);
        }
    }

    public List<String> selectedFields() {
        return selectedFields;
    }
}
