package com.aihoo.domain.logistics.dto.sf;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {

    
    @JSONField(name = "language")
    private String language;

    
    @JSONField(name = "orderId")
    private String orderId;

    
    @JSONField(name = "customsInfo")
    private CustomsInfo customsInfo;

    
    @JSONField(name = "cargoDetails")
    private List<CargoDetail> cargoDetails;

    
    @JSONField(name = "cargoDesc")
    private String cargoDesc;

    
    @JSONField(name = "extraInfoList")
    private List<ExtraInfo> extraInfoList;

    
    @JSONField(name = "serviceList")
    private List<ServiceItem> serviceList;

    
    @JSONField(name = "contactInfoList")
    private List<ContactInfo> contactInfoList;

    
    @JSONField(name = "monthlyCard")
    private String monthlyCard;

    
    @JSONField(name = "payMethod")
    private Integer payMethod;

    
    @JSONField(name = "expressTypeId")
    private Integer expressTypeId;

    
    @JSONField(name = "parcelQty")
    private Integer parcelQty;

    
    @JSONField(name = "totalLength")
    private Double totalLength;

    
    @JSONField(name = "totalWidth")
    private Double totalWidth;

    
    @JSONField(name = "totalHeight")
    private Double totalHeight;

    
    @JSONField(name = "volume")
    private Double volume;

    
    @JSONField(name = "totalWeight")
    private Double totalWeight;

    
    @JSONField(name = "totalNetWeight")
    private String totalNetWeight;

    
    @JSONField(name = "sendStartTm")
    private String sendStartTm;

    
    @JSONField(name = "isDocall")
    private Integer isDocall;

    
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