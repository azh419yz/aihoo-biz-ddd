package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 网易云信配置文件
 * IM服务端所有接口都只支持POST请求；
 * 所有接口请求Content-Type类型为：application/x-www-form-urlencoded;charset=utf-8；
 * 所有接口返回类型为JSON，同时进行UTF-8编码。
 *
 * @author Lenovo
 */
@Component
@ConfigurationProperties(prefix = NeteaseProperties.PREFIX)
@Data
public class NeteaseProperties {
    protected static final String PREFIX = "netease";
    /**
     * AppKey	开发者平台分配的appkey
     * Nonce	随机数（最大长度128个字符）
     * CurTime	当前UTC时间戳，从1970年1月1日0点0 分0 秒开始到现在的秒数(String)
     * CheckSum	SHA1(AppSecret + Nonce + CurTime)，三个参数拼接的字符串，进行SHA1哈希计算，转化成16进制字符(String，小写)
     */
    private String appKey;
    private String appSecret;
    /**
     * 创建云信账号url
     */
    private String createUrl;
}
