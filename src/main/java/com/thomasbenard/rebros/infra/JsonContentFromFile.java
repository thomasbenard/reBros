package com.thomasbenard.rebros.infra;

import com.thomasbenard.rebros.JsonContent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class JsonContentFromFile extends JsonContent {

    JsonContentFromFile(String filePath) throws IOException {
        super(new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8));
    }

}
