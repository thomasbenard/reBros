package com.thomasbenard.rebros.infra;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class MainController {

    private final String data = "{id:1, first_name:\"Jean\", last_name:\"Bonneau}\"}";

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!\n";
    }

}