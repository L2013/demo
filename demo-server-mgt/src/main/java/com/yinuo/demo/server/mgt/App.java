package com.yinuo.demo.server.mgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liang
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        // 本应用
        "com.yinuo.demo.server.mgt",
        // 核心业务
        "com.yinuo.demo.domain",
        // 底层供应
        "com.yinuo.demo.impl.dao"
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
