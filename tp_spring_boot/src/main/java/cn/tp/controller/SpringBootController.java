package cn.tp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date:2018/12/31
 * Author
 * DESC:
 */
@RestController
@RequestMapping("/testBoot")
public class SpringBootController {

    //@Value("${url}")
    @Autowired
    private Environment environment;
    @RequestMapping("/test")
    public String  test(){
        return "Helloworder spring-boot"+environment.getProperty("url");
    }
}

