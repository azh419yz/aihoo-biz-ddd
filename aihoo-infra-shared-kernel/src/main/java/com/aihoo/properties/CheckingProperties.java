package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "checking")
public class CheckingProperties {
    private String saveUrl;
    private String saveForceUrl;
}
