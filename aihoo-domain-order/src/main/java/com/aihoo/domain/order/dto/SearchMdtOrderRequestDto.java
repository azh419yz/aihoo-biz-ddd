package com.aihoo.domain.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchMdtOrderRequestDto {

    private String hosSickName;

    private String receiveName;

    private String receivePhone;

    private String orderId;

    private String preId;

    private Integer medicineStatus;

    private String drugstoreId;

    private String status;

    private String payStartTime;

    private String payEndTime;

    private Boolean havePic;

    private List<String> statusList;
}