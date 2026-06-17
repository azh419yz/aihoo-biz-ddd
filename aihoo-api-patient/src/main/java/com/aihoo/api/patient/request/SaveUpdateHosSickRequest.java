package com.aihoo.api.patient.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 新增/修改就诊人请求（迁自 patient-api 的 SaveUpdateHosSickRequest）。
 */
@Data
@Schema(description = "新增/修改就诊人请求")
public class SaveUpdateHosSickRequest {

    public interface Save {
    }

    public interface Check {
    }

    public interface Update {
    }

    @Schema(description = "就诊人ID(修改时必填)", example = "12")
    @NotBlank(message = "就诊人ID不能为空", groups = Update.class)
    private String id;

    @Schema(description = "姓名", example = "张三")
    @NotBlank(message = "姓名不能为空", groups = {Check.class, Save.class})
    private String name;

    @Schema(description = "身份证号", example = "31000000000000000x")
    @NotBlank(message = "身份证号不能为空", groups = {Check.class, Save.class})
    private String idCard;

    @Schema(description = "性别(1男 2女)", example = "1")
    @NotBlank(message = "性别不能为空", groups = Save.class)
    private String sex;

    @Schema(description = "出生日期(yyyy-MM-dd)", example = "2000-01-01")
    @NotNull(message = "出生日期不能为空", groups = Save.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
}