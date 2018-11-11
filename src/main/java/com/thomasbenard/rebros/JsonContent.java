package com.thomasbenard.rebros;

import org.json.JSONObject;

public class JsonContent implements Content {
    private JSONObject jsonObject;

    public JsonContent(String inputData) {
        jsonObject = new JSONObject(inputData);
    }

    public String get(String id) {
        return jsonObject.get(id).toString();
    }
}
