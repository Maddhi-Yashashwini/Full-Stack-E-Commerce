package com.example.Ecommerce.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Test {

    @GetMapping("/merchant/test")
    public String adminTest() {
        return "merchant access granted";
    }

    @GetMapping("/customer/test")
    public String customerTest() {
        return "Customer access granted";
    }
}
