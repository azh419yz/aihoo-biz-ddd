package com.aihoo.api.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.aihoo")
@MapperScan(basePackages = "com.aihoo.domain.*.mapper")
public class AdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class, args);
    }
}