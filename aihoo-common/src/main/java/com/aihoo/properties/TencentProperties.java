package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent")
public class TencentProperties {
    private String sdkappid;
    private String privstr;
    private String adminidentifier;
}
