package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 医生端-问诊详情 DTO（迁自 doctor-api: HosOrder）。
 *
 * <p>由 {@code HosVisitService.visitData} 返回，含 makeVisitbtnJson 业务方法。
 */
@Data
@Schema(description = "医生端-问诊详情")
public class HosOrderDto {

    @Schema(description = "订单ID")
    private String orderId;

    @Schema(description = "患者ID")
    private String patientId;

    @Schema(description = "医生ID")
    private String doctorId;

    @Schema(description = "医生建议")
    private String doctorAdvice;

    @Schema(description = "订单号")
    private String orderNum;

    @Schema(description = "问诊类型")
    private String type;

    @Schema(description = "就诊人ID")
    private String hosSickId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "年龄")
    private String age;

    @Schema(description = "自述/病情")
    private String content;

    @Schema(description = "支付时间")
    private String payTime;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "完成时间")
    private String endTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "倒计时")
    private String countDown;

    @Schema(description = "首诊情况")
    private String firstVisit;

    @Schema(description = "地区CODE")
    private String areaCode;

    @Schema(description = "地区名称")
    private String areaName;

    @Schema(description = "头像")
    private String headImg;

    @Schema(description = "五星评价")
    private String fiveStar;

    @Schema(description = "问诊图片列表")
    private List<Object> imgList;

    @Schema(description = "按钮 JSON")
    private String visitbtnJson;

    public void makeVisitbtnJson() {
        // 占位：老 doctor-api 用 reflectasm/jackson 生成按钮 JSON。
        // DDD 阶段保持空，由 controller 根据 status 自行生成（不在此处强实现）。
        this.visitbtnJson = "{}";
    }
}
