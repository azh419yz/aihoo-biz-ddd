package com.aihoo.domain.logistics.dto.sf;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 顺丰下单接口 (EXP_RECE_CREATE_ORDER) - 业务请求参数。
 */
@Data
public class CreateOrderDto {

    /** 语言版本 */
    @JSONField(name = "language")
    private String language;

    /** 订单号 */
    @JSONField(name = "orderId")
    private String orderId;

    /** 报关信息 (跨境件必填) */
    @JSONField(name = "customsInfo")
    private CustomsInfo customsInfo;

    /** 货物明细列表 */
    @JSONField(name = "cargoDetails")
    private List<CargoDetail> cargoDetails;

    /** 货物描述 */
    @JSONField(name = "cargoDesc")
    private String cargoDesc;

    /** 扩展信息列表 (键值对形式) */
    @JSONField(name = "extraInfoList")
    private List<ExtraInfo> extraInfoList;

    /** 增值服务列表 */
    @JSONField(name = "serviceList")
    private List<ServiceItem> serviceList;

    /** 联系人信息列表 */
    @JSONField(name = "contactInfoList")
    private List<ContactInfo> contactInfoList;

    /** 月结卡号 */
    @JSONField(name = "monthlyCard")
    private String monthlyCard;

    /** 支付方式 */
    @JSONField(name = "payMethod")
    private Integer payMethod;

    /** 快递产品ID */
    @JSONField(name = "expressTypeId")
    private Integer expressTypeId;

    /** 包裹数量 */
    @JSONField(name = "parcelQty")
    private Integer parcelQty;

    /** 总长度 (cm) */
    @JSONField(name = "totalLength")
    private Double totalLength;

    /** 总宽度 (cm) */
    @JSONField(name = "totalWidth")
    private Double totalWidth;

    /** 总高度 (cm) */
    @JSONField(name = "totalHeight")
    private Double totalHeight;

    /** 总体积 */
    @JSONField(name = "volume")
    private Double volume;

    /** 总重量 (kg) */
    @JSONField(name = "totalWeight")
    private Double totalWeight;

    /** 总净重 (kg) */
    @JSONField(name = "totalNetWeight")
    private String totalNetWeight;

    /** 期望取件开始时间 */
    @JSONField(name = "sendStartTm")
    private String sendStartTm;

    /** 是否电联 */
    @JSONField(name = "isDocall")
    private Integer isDocall;

    /** 是否签回单 */
    @JSONField(name = "isSignBack")
    private Integer isSignBack;

    @Data
    public static class CustomsInfo {
        @JSONField(name = "declaredValue")
        private Double declaredValue;
    }

    @Data
    public static class CargoDetail {
        @JSONField(name = "amount")
        private Double amount;

        @JSONField(name = "count")
        private Double count;

        @JSONField(name = "currency")
        private String currency;

        @JSONField(name = "goodPrepardNo")
        private String goodPrepardNo;

        @JSONField(name = "hsCode")
        private String hsCode;

        @JSONField(name = "name")
        private String name;

        @JSONField(name = "productRecordNo")
        private String productRecordNo;

        @JSONField(name = "sourceArea")
        private String sourceArea;

        @JSONField(name = "taxNo")
        private String taxNo;

        @JSONField(name = "unit")
        private String unit;

        @JSONField(name = "weight")
        private Double weight;
    }

    @Data
    public static class ExtraInfo {
        @JSONField(name = "attrName")
        private String attrName;

        @JSONField(name = "attrVal")
        private String attrVal;
    }

    @Data
    public static class ServiceItem {
        @JSONField(name = "name")
        private String name;

        @JSONField(name = "value")
        private String value;
    }

    @Data
    public static class ContactInfo {
        @JSONField(name = "address")
        private String address;

        @JSONField(name = "city")
        private String city;

        @JSONField(name = "contact")
        private String contact;

        @JSONField(name = "contactType")
        private Integer contactType;

        @JSONField(name = "country")
        private String country;

        @JSONField(name = "county")
        private String county;

        @JSONField(name = "mobile")
        private String mobile;

        @JSONField(name = "postCode")
        private String postCode;

        @JSONField(name = "province")
        private String province;

        @JSONField(name = "tel")
        private String tel;

        @JSONField(name = "company")
        private String company;
    }
}