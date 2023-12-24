package com.neo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
    @RequestMapping("/")
    public String index() {
        return "Hello Spring Boot 3.x!";
    }

    @RequestMapping("/upper")
    public String upperCase(String word){

        return word.toUpperCase();
    }
}