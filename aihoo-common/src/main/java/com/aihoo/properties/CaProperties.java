package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ca")
public class CaProperties {
    private String appId;
    private String openUrl;
    private String callBack;
    private String privateStr;
}
