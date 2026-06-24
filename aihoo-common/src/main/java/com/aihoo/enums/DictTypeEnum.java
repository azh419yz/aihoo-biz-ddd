package com.aihoo.enums;

public enum DictTypeEnum {
    BANNER("BRAND类型", "BRAND_TYPE"),
    HOS_GRADE("医院等级", "HOS_GRADE"),
    HOS_LEVEL("医院级别", "HOS_LEVEL"),
    HOS_ATT("医院属性", "HOS_ATT"),
    DOCT_TITLE("医生职称", "DOCT_TITLE"),
    RYLB("人员类别", "YWRYLB"),
    POSITION("职务编码", "POSITION"),
    PAPERS("证件类型编码", "CERT_TYPE"),
    WEEK("星期", "WEEK"),
    HOS_CATE("医院类型", "HOS_CATE"),
    DRUG_DOS("药物剂型", "YWJX"),
    MED_UNIT("用药单位", "MED_UNIT"),
    PACK_UNIT("包装单位编码", "PACK_UNIT"),
    FREQ_MED("默认用药频次编码", "YYPC"),
    ROUTE_ADMI("默认用药途径编码", "ROUTE_ADMI"),
    YYPC("默认用药频次编码", "YYPC"),
    JYBZ("基药标识", "JYBZ");

    private String typeName;
    private String type;

    DictTypeEnum(String typeName, String type) {
        this.typeName = typeName;
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}