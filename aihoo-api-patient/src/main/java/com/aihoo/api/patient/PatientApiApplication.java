package com.aihoo.api.patient;

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

@SpringBootApplication(scanBasePackages = {"com.aihoo.api.patient", "com.aihoo.domain", "com.aihoo"})
@MapperScan(basePackages = {"com.aihoo.domain.*.mapper"})
@ComponentScan(basePackages = {
        "com.aihoo.api.patient",
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
        info = @Info(title = "Patient API", version = "1.0", description = "Patient API Documentation"),
        security = @SecurityRequirement(name = "accessToken"),
        servers = {
                @Server(url = "https://patient.heouai.com", description = "云开发服务器（HTTPS）"),
                @Server(url = "http://localhost:8082", description = "本地开发服务器（HTTP）")
        }
)
public class PatientApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientApiApplication.class, args);
        System.out.print("----------------------启动成功 start success----------------------");
    }
}
