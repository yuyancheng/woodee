package com.wissy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wissy.dao.UserInfo;
import com.wissy.pojo.User;

@RestController
public class Portel extends Object {
    @GetMapping("/hi")
    public User hi(@RequestParam(value = "uid", required = true) int uid) {
        //return "hi, I'm springboot22222 !";
        return new UserInfo().getUsrInfo(uid);
    }
}