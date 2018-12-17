package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Date:2018/12/15
 * Author
 * DESC:
 */
@RestController
@RequestMapping("login")
public class LoginController {


    @GetMapping("/getUsername")
    public Map<String,Object> getUsername(){
         Map<String,Object> userMap=new HashMap<>();
         //得到用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userMap.put("username",username);
        return userMap;

    }
}
