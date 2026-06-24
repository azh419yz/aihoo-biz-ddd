package com.aihoo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImServiceApiEnum {

    SEND_MSG("send_msg", "${MSG}"),
    MODIFY_C2C_MSG("modify_c2c_msg", "${MSG}");

    private final String apiName;
    private final String apiTemplate;
}
