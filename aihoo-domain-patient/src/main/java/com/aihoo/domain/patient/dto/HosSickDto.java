package com.aihoo.domain.patient.dto;

import com.aihoo.domain.patient.entity.HosSick;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 就诊人 dto（domain 层用，承载 service 返回值；由 controller 转 vo）。
 */
@Data
@Schema(description = "就诊人 DTO")
public class HosSickDto {

    private String id;
    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String name;
    private String idCard;
    private String sex;
    private String age;
    private String mobile;
    private String isDefault;
    private String isDelete;
    private String address;
    private Date birthday;
    private String imUserId;
    private String imUserSig;

    private String avatar;
    private String status;
    private String imGroupId;

    private Integer idCardVerify;
    private String pastHistory;
    private String area;
    private String areaName;
    private String allergyHistory;
    private String height;
    private String weight;
    private List<String> tongueImages;
    private List<String> faceImages;
    private List<String> medicalRecordImages;

    private List<HosVisitDto> visits;

    public static HosSickDto fromEntity(HosSick hosSick) {
        if (hosSick == null) return null;
        HosSickDto dto = new HosSickDto();
        dto.setId(hosSick.getId());
        dto.setCreateTime(hosSick.getCreateTime());
        dto.setUpdateTime(hosSick.getUpdateTime());
        dto.setPatientUserId(hosSick.getPatientUserId());
        dto.setName(hosSick.getName());
        dto.setIdCard(hosSick.getIdCard());
        dto.setSex(hosSick.getSex());
        dto.setAge(hosSick.getAge());
        dto.setMobile(hosSick.getMobile());
        dto.setIsDefault(hosSick.getIsDefault());
        dto.setIsDelete(hosSick.getIsDelete());
        dto.setAddress(hosSick.getAddress());
        dto.setBirthday(hosSick.getBirthday());
        dto.setImUserId(hosSick.getImUserId());
        dto.setImUserSig(hosSick.getImUserSig());
        return dto;
    }
}