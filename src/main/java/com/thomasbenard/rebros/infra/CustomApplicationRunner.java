package com.thomasbenard.rebros.infra;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Given arguments: ");
        for (String argument : args.getSourceArgs())
            System.out.println(argument);
    }
}
