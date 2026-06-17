package com.aihoo.domain.visit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "问诊订单对象")
public class HosVisitOrderVo {

    @Schema(description = "问诊单ID")
    private String id;

    @Schema(description = "医生ID")
    private String doctorId;

    @Schema(description = "医生身份证号")
    private String doctorIdCard;

    @Schema(description = "五星评分")
    private Integer fiveStar;

    @Schema(description = "医生姓名")
    private String doctorName;

    @Schema(description = "医生头像图片地址")
    private String doctorHeadImg;

    @Schema(description = "医院名称")
    private String hospitalName;

    @Schema(description = "科室负责人姓名")
    private String officeHolderName;

    @Schema(description = "科室名称")
    private String departName;

    @Schema(description = "总价格")
    private BigDecimal totalPrice;

    @Schema(description = "订单号")
    private String orderNum;

    @Schema(description = "问诊单类型")
    private String type;

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "患者Id")
    private String patientId;

    @Schema(description = "就诊人Id")
    private String sickId;

    @Schema(description = "就诊人姓名")
    private String sickName;

    @Schema(description = "就诊人年龄")
    private String age;

    @Schema(description = "就诊人性别")
    private String sex;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "最后一条医患问诊信息")
    private String msg;

    @Schema(description = "消息未读数量")
    private String msgPeerReadCount;

    @Schema(description = "问诊内容")
    private String content;

    @Schema(description = "im用户id")
    private String imUserId;

    @Schema(description = "im")
    private String imUserSig;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "群聊ID")
    private String imGroupId;

    @Schema(description = "状态上下级")
    private VisitStatusVo visitStatus;

    @Schema(description = "是否填写了健康状况")
    private Boolean hasHealthInfo;

    @Schema(description = "是否填写了基本状况")
    private Boolean hasBaseInfo;

    @Schema(description = "最后一条消息状态")
    private String lastMsgStatus;

    @Schema(description = "最后一条消息类型")
    private String lastMsgType;

    @Schema(description = "最后一条消息内容")
    private String lastMsgContent;

    @Schema(description = "最后一条消息发送者角色")
    private String lastMsgSenderRole;
}