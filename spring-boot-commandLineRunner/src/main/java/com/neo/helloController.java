package com.neo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class helloController implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("The helloController start to initialize ...");
    }
}