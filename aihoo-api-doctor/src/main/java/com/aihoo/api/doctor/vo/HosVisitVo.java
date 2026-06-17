package com.aihoo.api.doctor.vo;

import com.aihoo.domain.visit.entity.HosPrescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "医生端-问诊单 VO")
public class HosVisitVo {

    @Schema(description = "订单号")
    private String visitNo;

    @Schema(description = "订单id")
    private Long visitId;

    @Schema(description = "问诊ID（来自 entity）")
    private String id;

    @Schema(description = "自述")
    private String content;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "问诊类型")
    private String type;

    @Schema(description = "基本状况")
    private String baseInfo;

    @Schema(description = "健康状况")
    private String healthInfo;

    @Schema(description = "医生姓名")
    private String doctorName;

    @Schema(description = "医生头像")
    private String doctorHeadImg;

    @Schema(description = "处方列表")
    private List<HosPrescription> hosPrescriptions;
}
