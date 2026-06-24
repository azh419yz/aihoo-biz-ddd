package com.aihoo.domain.patient.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SaveUpdateHosSickDto {

    private String id;
    private String name;
    private String idCard;
    private String sex;
    private Date birthday;
}