package com.aihoo.domain.patient.dto;

import lombok.Data;

import java.util.Date;

/**
 * 新增/修改就诊人 dto（domain 层用）。
 */
@Data
public class SaveUpdateHosSickDto {

    private String id;
    private String name;
    private String idCard;
    private String sex;
    private Date birthday;
}