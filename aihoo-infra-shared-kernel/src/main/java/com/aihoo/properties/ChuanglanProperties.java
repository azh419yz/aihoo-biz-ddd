package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "chuanglan")
public class ChuanglanProperties {

    private App ios;
    private App android;

    @Data
    public static class App {
        private String appId;
        private String appKey;
    }
}
