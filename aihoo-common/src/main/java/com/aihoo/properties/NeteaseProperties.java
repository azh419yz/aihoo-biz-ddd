package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = NeteaseProperties.PREFIX)
@Data
public class NeteaseProperties {
    protected static final String PREFIX = "netease";
    
    private String appKey;
    private String appSecret;
    
    private String createUrl;
}
