package com.zjuse.bankingsystem.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("test")
public class testController {
    @GetMapping("hello")
    public String testHello() {
        return "hello, here is the group of A in software engineering basis";
    }
}
