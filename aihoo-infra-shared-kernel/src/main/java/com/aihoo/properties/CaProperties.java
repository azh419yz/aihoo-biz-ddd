package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CA 认证 配置
 *
 * @author lx
 * @date 2021-01-07
 */
@Data
@Component
@ConfigurationProperties(prefix = "ca")
public class CaProperties {
    private String appId;
    private String openUrl;
    private String callBack;
    private String privateStr;
}
