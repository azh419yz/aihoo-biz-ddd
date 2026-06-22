package com.aihoo.api.admin.vo;

import com.aihoo.domain.order.dto.PrescriptionDrugDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MdtOrderVo {

    @Schema(name = "id", description = "订单ID", example = "1")
    private String id;

    @Schema(name = "name", description = "名称", example = "张三")
    private String name;

    @Schema(name = "sex", description = "性别 0-女 1-男", example = "1")
    private String sex;

    @Schema(name = "age", description = "年龄", example = "18")
    private String age;

    @Schema(name = "preId", description = "处方ID", example = "1")
    private String preId;

    @Schema(name = "drugstoreName", description = "药房名称", example = "同仁堂")
    private String drugstoreName;

    @Schema(name = "medicineStatusCode", description = "药态 1:中药饮片-自煎 2:中药饮片-代煎 3:颗粒", example = "2")
    private String medicineStatusCode;

    @Schema(name = "doseNumber", description = "全部剂数", example = "14")
    private String doseNumber;

    @Schema(name = "drugList", description = "药品", example = "[{'id': '1', 'name': '葛根', 'price': '2.15', 'count': '10'}]")
    private List<PrescriptionDrugDTO> drugList;

    @Schema(name = "status", description = "状态", example = "1")
    private String status;

    @Schema(name = "hosSickRemark", description = "患者备注", example = "加急")
    private String hosSickRemark;

    @Schema(name = "remark", description = "药师备注", example = "慢点")
    private String remark;

    @Schema(name = "picList", description = "药品照片", example = "http://xxx/1.jpg,http://xxx/2.jpg")
    private List<String> picList;

    @Schema(name = "pdfFlag", description = "打印PDF", example = "已打印")
    private String pdfFlag;

    @Schema(name = "expressFlag", description = "打印快递标签", example = "已打印")
    private String expressFlag;

    @Schema(name = "receiveName", description = "收件人姓名", example = "张三")
    private String receiveName;

    @Schema(name = "receivePhone", description = "收件人电话", example = "18600001111")
    private String receivePhone;

    @Schema(name = "receiveArea", description = "收件人区域", example = "北京,北京,海淀区")
    private String receiveArea;

    @Schema(name = "receiveAddress", description = "收件人地址", example = "山东路1号3单元102")
    private String receiveAddress;

    @Schema(name = "payTime", description = "支付时间", example = "2020-01-01 10:00:00")
    private String payTime;

}