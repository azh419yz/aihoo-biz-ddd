package com.aihoo.domain.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "医生通讯录 DTO")
public class DoctorDirectoryDto {
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "名称")
    private String sickName;
    @Schema(description = "病人id")
    private Long sickId;
    @Schema(description = "用户id")
    private Long patientUserId;
    @Schema(description = "性别")
    private String sickSex;
    @Schema(description = "年龄")
    private String sickAge;
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "添加时间")
    private String saveTime;
    @Schema(description = "来源 1:扫码  2:购买问诊卡")
    private Integer source;
}
