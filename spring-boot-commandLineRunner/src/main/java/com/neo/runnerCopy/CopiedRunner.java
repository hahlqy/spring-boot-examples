package com.neo.runnerCopy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CopiedRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("The CopiedRunner start to initialize ...");
    }
}
