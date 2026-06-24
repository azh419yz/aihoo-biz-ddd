package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {
    private String appId;
    private String secret;
    private String mchId;
    private String weiXinBody;
    private String weiXinNotifyUrl;
    private String weiXinPartnerKey;
    private String unifiedOrderUrl;
    private String callback;
    private String certPath;
    private String apiBaseUrl = "https://api.weixin.qq.com";
}
