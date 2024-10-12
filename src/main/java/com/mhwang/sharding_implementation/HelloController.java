package com.mhwang.sharding_implementation;

import com.mhwang.sharding_implementation.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public String index() {

        return "Hello From Spring Boot!";
    }
}
