package com.aihoo.domain.im.dto;

import lombok.Data;

@Data
public class ImContactRequestDto {
    private String From_Account;
    private Integer TimeStamp;
    private Integer StartIndex;
    private Integer TopTimeStamp;
    private Integer TopStartIndex;
    private Integer AssistFlags = 15;
}
