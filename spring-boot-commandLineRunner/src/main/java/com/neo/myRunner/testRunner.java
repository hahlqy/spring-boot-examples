package com.neo.myRunner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("The testRunner3 start to initialize ...");
    }
}
