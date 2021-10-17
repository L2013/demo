package com.yinuo.demo.server.mgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liang
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.yinuo.demo.server.mgt", "com.yinuo.demo.domain", "com.yinuo.demo.res.database"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
