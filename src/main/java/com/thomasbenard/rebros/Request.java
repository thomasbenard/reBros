package com.thomasbenard.rebros;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Request {
    private List<String> selectedFields = new ArrayList<>();

    public void select(String... identifier) {
        selectedFields.addAll(Arrays.asList(identifier));
    }

    List<String> selectedFields() {
        return selectedFields;
    }
}
