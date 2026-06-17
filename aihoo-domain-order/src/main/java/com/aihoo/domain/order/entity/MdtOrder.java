package com.aihoo.domain.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_mdt_order")
@Schema(description = "mdt订单表")
public class MdtOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String hosSickId;
    private String adminId;
    private String mdtTeamId;
    private String mdtCode;
    private String mdtName;
    private String name;
    private String idCard;
    private String sex;
    private String age;
    private String mobile;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String payOrderNum;
    private String status;
    private String msg;
    private String orderNum;
    private String doctorUserId;
    private String doctorUserName;
    private String doctorAdvice;
    private String mdtAppointmentTime;
    private String mdtStartTime;
    private String mdtEndTime;
    private String mdtDateStr;
    private String mdtTimeStr;
    private String outFileRemark;
    private String checkFileRemark;
    private String videoFileRemark;
    private String registerPrice;
    private String regOrderNum;
    private String regPayType;
    private String regPayTime;
    private String attendingDoctorName;
    private String consultationDoctorName;
    private String isPay;
    private String isRegPay;
    private String isCount;
    private String imDoctorNumber;
    private String mdtHospital;
    private String moderator;
    private String contactWay;
    private String mdtType;
    private String mdtRemark;
    private String mdtRoomUrl;
    private String mdtRoomId;
    private String isCanChat;
    private String isSendSuccess;
    private String reportDoctorId;
    private String prescriptionDoctorId;
    private String isAgree;
    private String patientIntentionTime;
    private String preId;
    private String medicineStatusCode;
    private String drugstoreId;
    private String hosSickRemark;
    private String remark;
    private String pic;
    private String pdfFlag;
    private String expressFlag;
    private String receiveName;
    private String receivePhone;
    private String receiveArea;
    private String receiveAddress;
    private String expressNo;
}
