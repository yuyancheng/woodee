package com.nix.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortelController {
    @RequestMapping(method = RequestMethod.GET, value = "/sayHi")
    public String sayHi() {
        return "Hello world!";
    }
}
