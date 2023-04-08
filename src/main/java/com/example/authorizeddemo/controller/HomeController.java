package com.example.authorizeddemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/hello")
    public String home(){
        return "Hello!";
    }
    @GetMapping("/greeting")
    public String greeting(){
        return "Hello, How are you?";
    }
}
