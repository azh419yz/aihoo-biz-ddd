package com.aihoo.alicloud;

import com.aihoo.properties.AliCloudProperties;
import com.alibaba.fastjson2.JSON;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyRequest;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponse;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponseBody;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AliCloudComponent {

    private final AliCloudProperties aliCloudProperties;

    private Config getConfig() {
        return new Config()
                .setAccessKeyId(aliCloudProperties.getAk())
                .setAccessKeySecret(aliCloudProperties.getSk());
    }

    
    public boolean verifyIdentity(String name, String idCard) {
        Config config = getConfig();
        config.endpoint = aliCloudProperties.getAuth().getEndpoint();
        try {
            com.aliyun.cloudauth20190307.Client client = new com.aliyun.cloudauth20190307.Client(config);
            Id2MetaVerifyRequest request = new Id2MetaVerifyRequest()
                    .setParamType("normal")
                    .setIdentifyNum(idCard)
                    .setUserName(name);

            RuntimeOptions runtime = new RuntimeOptions();
            Id2MetaVerifyResponse response = client.id2MetaVerifyWithOptions(request, runtime);

            log.info("Identity verification response: {}", JSON.toJSONString(response));

            Id2MetaVerifyResponseBody body = response.getBody();
            if (!"200".equals(body.getCode())) {
                log.error("Identity verification failed: code={}, message={}", body.getCode(), body.getMessage());
                return false;
            }
            return body.getResultObject() != null && "1".equals(body.getResultObject().getBizCode());
        } catch (Exception e) {
            log.error("Identity verification failed", e);
            return false;
        }
    }

    
    public boolean sendSms(String phoneNumber, String templateParam) {
        Config config = getConfig();
        config.endpoint = aliCloudProperties.getSms().getEndpoint();
        try {
            com.aliyun.dysmsapi20170525.Client client = new com.aliyun.dysmsapi20170525.Client(config);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName(aliCloudProperties.getSms().getSignName())
                    .setTemplateCode(aliCloudProperties.getSms().getTemplateCode())
                    .setPhoneNumbers(phoneNumber)
                    .setTemplateParam(templateParam);
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);

            log.info("Send sms response: {}", JSON.toJSONString(response));

            SendSmsResponseBody body = response.getBody();
            if (!"OK".equals(body.getCode())) {
                log.error("Send SMS failed: code={}, message={}", body.getCode(), body.getMessage());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Send SMS failed", e);
            return false;
        }
    }
}
