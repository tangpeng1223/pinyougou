package com.pinyougou.manage.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/login")
public class LoginController {

    /**
     * 查询用户的信息
     *  $http.get("../login/getUsername.do");
     * @return Map<String,Object>
     */
    @GetMapping("/getUsername")
    public Map<String,Object> login(){
        Map<String ,Object> map=new HashMap<>();
        //获取存入在security中的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("username",username);
        return  map;

    }
}
