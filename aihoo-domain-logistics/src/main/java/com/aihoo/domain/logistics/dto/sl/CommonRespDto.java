package com.aihoo.domain.logistics.dto.sl;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class CommonRespDto {
    
    @JSONField(name = "success")
    private String success;

    
    @JSONField(name = "errorCode")
    private String errorCode;

    
    @JSONField(name = "errorMsg")
    private String errorMsg;

    
    @JSONField(name = "msgData")
    private String msgData;
}