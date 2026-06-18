package com.aihoo.api.doctor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @author: sunjianbo
 * @date: 2019/7/23 9:58
 */
@SpringBootApplication(scanBasePackages = {"com.aihoo.api.doctor", "com.aihoo.domain", "com.aihoo"})
@MapperScan(basePackages = {"com.aihoo.domain.*.mapper"})
@ComponentScan(basePackages = {
        "com.aihoo.api.doctor",
        "com.aihoo.domain",
        "com.aihoo.util",
        "com.aihoo.excel",
        "com.aihoo.redis",
        "com.aihoo.config",
        "com.aihoo.properties",
        "com.aihoo.security",
        "com.aihoo.constant", "com.aihoo.oss",
        "com.aihoo.wechat",
        "com.aihoo.alicloud"
})
@SecurityScheme(name = "accessToken", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(
        info = @Info(title = "Doctor API", version = "1.0", description = "Doctor API Documentation"),
        security = @SecurityRequirement(name = "accessToken"),
        servers = {
                @Server(url = "https://doctor.heouai.com", description = "云开发服务器（HTTPS）"),
                @Server(url = "http://localhost:8081", description = "本地开发服务器（HTTP）")
        }
)
public class DoctorApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApiApplication.class, args);
        System.out.print("----------------------启动成功 start success----------------------");
    }
}
