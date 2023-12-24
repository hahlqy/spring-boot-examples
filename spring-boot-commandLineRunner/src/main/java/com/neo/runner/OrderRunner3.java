package com.neo.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderRunner3 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("The Runner3 start to initialize ...");
    }
}
