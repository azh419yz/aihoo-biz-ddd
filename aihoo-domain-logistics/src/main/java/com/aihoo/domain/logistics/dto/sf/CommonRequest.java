package com.aihoo.domain.logistics.dto.sf;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 顺丰公共请求。
 */
@Data
public class CommonRequest {
    @JSONField(name = "partnerID")
    private String partnerID;

    @JSONField(name = "requestID")
    private String requestID;

    @JSONField(name = "serviceCode")
    private String serviceCode;

    @JSONField(name = "timestamp")
    private Long timestamp;

    @JSONField(name = "msgDigest")
    private String msgDigest;

    @JSONField(name = "accessToken")
    private String accessToken;

    @JSONField(name = "msgData")
    private String msgData;
}