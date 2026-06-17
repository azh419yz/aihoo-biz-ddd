package com.aihoo.domain.visit.dto;

import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.entity.HosVisitImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class HosVisitVo extends HosVisit {
    private String doctorName;
    private String doctorHeadImg;
    private String hospitalName;
    private String officeHolderName;
    private String departName;
    private String sickName;
    private String imUserId;
    private String imUserSig;
    private String fiveStarProportion;
    private String orderNumber;

    private List<HosVisitImg> imgs;

    @Schema(name = "hosPrescriptions", description = "处方")
    List<HosPrescription> hosPrescriptions;

    @Schema(description = "是否填写了健康状况")
    private Boolean hasHealthInfo;
    @Schema(description = "是否填写了基本状况")
    private Boolean hasBaseInfo;
}