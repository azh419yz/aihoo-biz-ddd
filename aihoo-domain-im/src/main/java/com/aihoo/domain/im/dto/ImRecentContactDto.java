package com.aihoo.domain.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "IM当前会话列表")
public class ImRecentContactDto {
    @Schema(description = "结束标识")
    private Integer CompleteFlag;
    @Schema(description = "起始时间")
    private Integer TimeStamp;
    @Schema(description = "起始位置")
    private Integer StartIndex;
    @Schema(description = "置顶起始时间")
    private Integer TopTimeStamp;
    @Schema(description = "置顶起始位置")
    private Integer TopStartIndex;
    @Schema(description = "处理结果")
    private String ActionStatus;
    @Schema(description = "错误码")
    private Integer ErrorCode;
    @Schema(description = "错误信息")
    private String ErrorInfo;
    @Schema(description = "展示信息")
    private String errorDisplay;
    @Schema(description = "会话列表")
    private List<ImSessionItemDto> SessionItem;
}
