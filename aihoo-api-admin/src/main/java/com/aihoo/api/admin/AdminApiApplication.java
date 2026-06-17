package com.aihoo.api.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.aihoo.api.admin", "com.aihoo.domain", "com.aihoo"})
@MapperScan(basePackages = {"com.aihoo.domain.*.mapper", "com.aihoo.push"})
@ComponentScan(basePackages = {
        "com.aihoo.api.admin",
        "com.aihoo.domain",
        "com.aihoo.util",
        "com.aihoo.excel",
        "com.aihoo.redis",
        "com.aihoo.config",
        "com.aihoo.properties",
        "com.aihoo.security",
        "com.aihoo.constant", "com.aihoo.oss",
        "com.aihoo.wechat",
        "com.aihoo.push"
})
public class AdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class, args);
    }
}