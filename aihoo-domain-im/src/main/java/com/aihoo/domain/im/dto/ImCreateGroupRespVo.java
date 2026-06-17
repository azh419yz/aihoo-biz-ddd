package com.aihoo.domain.im.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "im创建群组返回信息")
public class ImCreateGroupRespVo extends ImRespVo {
    @Schema(description = "分组ID")
    @JSONField(name = "GroupId")
    private String groupId;
}
