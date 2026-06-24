package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayProperties {
    private String appId;
    private String priKey;
    private String charset;
    private String publicKey;
    private String notifyUrl;
    private String subject;
    private String pId;
    private String targetId;
    private String alipayRefundUrl;
}
