package com.aihoo.domain.doctor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 医生用户表（迁移自 aihoo-biz-service/aihoo-doctor-api 的 DoctorUser）。
 *
 * @author mcp
 * @since 2020-09-18
 */
@Data
@TableName("t_doctor_user")
public class DoctorUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String createUserId;
    private String mobile;
    private String headImg;
    private String name;
    private String tag;
    /**
     * 排序
     */
    @TableField("`index`")
    private String index;
    private String memberNum;
    private String hospitalId;
    private String hospitalName;
    private String departId;
    private String departCode;
    private String departName;
    private String officeHolderCode;
    private String officeHolderName;
    private String beGoodAtText;
    private String introductionText;
    private String status;
    private String isAuth;
    private String token;
    private String caNumber;

    @TableField(select = false)
    private String caCert;

    private String personTypeCode;
    private String personTypeName;
    private String positionCode;
    private String positionName;
    private String papersCode;
    private String papersName;
    private String papersNumbers;
    private String userSig;
    private String isCancel;
    private String sex;
    private String age;
    private String birthday;
    /**
     * 成就
     */
    private String achievement;
    /**
     * '会诊医生类型   助理医生 ASSISTANT  会诊医生 CONSULTANT'
     */
    private String doctorType;

    private String medicalLicensePageOne;
    private String medicalLicensePageTwo;
    private String medicalLicenseNo;
    private String medicalLicenseIssueDate;
    private String practiceCertificatePageOne;
    private String practiceCertificatePageTwo;
    private String practiceCertificateNo;
    private String practiceCertificateIssueDate;
    private String area;
}