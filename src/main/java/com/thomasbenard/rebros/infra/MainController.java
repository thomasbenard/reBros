package com.thomasbenard.rebros.infra;

import com.thomasbenard.rebros.Content;
import com.thomasbenard.rebros.Matches;
import com.thomasbenard.rebros.ReBros;
import com.thomasbenard.rebros.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainController {

    @Value("${file.json}")
    private String jsonFile;

    @RequestMapping("/")
    public String index(String select, String where) {
        try {
            Content content = new JsonContentFromFile(jsonFile);
            ReBros reBros = new ReBros(content);
            Request request = new RequestFromWeb(select, where);
            Matches result = reBros.run(request);
            return result.serialize(new ToStringMatchesSerializer());
        } catch (IOException e) {
            return "File Not Found";
        }
    }

}