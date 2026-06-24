package com.aihoo.domain.logistics.dto.sl;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRespDto {

    @JSONField(name = "orderId")
    private String orderId;

    @JSONField(name = "originCode")
    private String originCode;

    @JSONField(name = "destCode")
    private String destCode;

    @JSONField(name = "filterResult")
    private Integer filterResult;

    @JSONField(name = "remark")
    private String remark;

    @JSONField(name = "url")
    private String url;

    @JSONField(name = "paymentLink")
    private String paymentLink;

    @JSONField(name = "isUpstairs")
    private Object isUpstairs;

    @JSONField(name = "isSpecialWarehouseService")
    private Object isSpecialWarehouseService;

    @JSONField(name = "mappingMark")
    private Object mappingMark;

    @JSONField(name = "agentMailno")
    private Object agentMailno;

    @JSONField(name = "returnExtraInfoList")
    private Object returnExtraInfoList;

    @JSONField(name = "waybillNoInfoList")
    private List<WaybillNoInfo> waybillNoInfoList;

    @JSONField(name = "routeLabelInfo")
    private List<RouteLabelInfo> routeLabelInfo;

    @JSONField(name = "contactInfoList")
    private Object contactInfoList;

    @Data
    public static class WaybillNoInfo {
        @JSONField(name = "waybillType")
        private Integer waybillType;

        @JSONField(name = "waybillNo")
        private String waybillNo;
    }

    @Data
    public static class RouteLabelInfo {
        @JSONField(name = "code")
        private String code;

        @JSONField(name = "routeLabelData")
        private RouteLabelData routeLabelData;

        @JSONField(name = "message")
        private String message;
    }

    @Data
    public static class RouteLabelData {
        @JSONField(name = "waybillNo")
        private String waybillNo;

        @JSONField(name = "sourceTransferCode")
        private String sourceTransferCode;

        @JSONField(name = "sourceCityCode")
        private String sourceCityCode;

        @JSONField(name = "sourceDeptCode")
        private String sourceDeptCode;

        @JSONField(name = "destCityCode")
        private String destCityCode;

        @JSONField(name = "destDeptCode")
        private String destDeptCode;

        @JSONField(name = "destTransferCode")
        private String destTransferCode;

        @JSONField(name = "destRouteLabel")
        private String destRouteLabel;

        @JSONField(name = "proName")
        private String proName;

        @JSONField(name = "cargoTypeCode")
        private String cargoTypeCode;

        @JSONField(name = "limitTypeCode")
        private String limitTypeCode;

        @JSONField(name = "expressTypeCode")
        private String expressTypeCode;

        @JSONField(name = "proCode")
        private String proCode;

        @JSONField(name = "twoDimensionCode")
        private String twoDimensionCode;

        @JSONField(name = "checkCode")
        private String checkCode;

        @JSONField(name = "destGisDeptCode")
        private String destGisDeptCode;

        @JSONField(name = "printFlag")
        private String printFlag;

        @JSONField(name = "printIcon")
        private String printIcon;

        @JSONField(name = "errMsg")
        private String errMsg;
    }
}