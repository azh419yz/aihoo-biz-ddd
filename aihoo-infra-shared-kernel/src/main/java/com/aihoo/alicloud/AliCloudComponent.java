package com.aihoo.alicloud;

import org.springframework.stereotype.Component;

@Component
public class AliCloudComponent {
    public boolean sendSms(String phone, String templateParam) {
        return true;
    }

    public boolean verifyIdentity(String name, String idCard) {
        // TODO: 接入阿里云身份认证 API 后替换
        return true;
    }
}
