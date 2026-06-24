package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "test.user")
public class TestProperties {
    private String mobile;
    private String code;
}
