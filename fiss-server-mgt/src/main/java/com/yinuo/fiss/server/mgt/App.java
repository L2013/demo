package com.yinuo.fiss.server.mgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liang
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.yinuo.fiss.server.mgt", "com.yinuo.fiss.domain", "com.yinuo.fiss.res.database"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
