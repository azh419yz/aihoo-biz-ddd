package com.aihoo.domain.logistics.dto.sl;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 顺丰通用返回结果。
 */
@Data
public class CommonRespVo {
    /** 请求成功标志 */
    @JSONField(name = "success")
    private String success;

    /** 错误编码 (S0000 表示成功) */
    @JSONField(name = "errorCode")
    private String errorCode;

    /** 错误描述 */
    @JSONField(name = "errorMsg")
    private String errorMsg;

    /** 返回的详细数据 */
    @JSONField(name = "msgData")
    private String msgData;
}