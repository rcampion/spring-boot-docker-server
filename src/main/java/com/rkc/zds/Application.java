package com.rkc.zds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan("com.rkc.zds")
@SpringBootApplication
// @RestController
public class Application {
/*
    @RequestMapping("/")
    public String home() {
        return "Hello Docker World";
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
