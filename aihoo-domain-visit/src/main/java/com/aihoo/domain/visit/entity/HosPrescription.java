package com.aihoo.domain.visit.entity;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;

/**
 * 处方表（迁自 patient-api 的 HosPrescription）。
 */
@Data
@TableName("t_hos_prescription")
public class HosPrescription implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String patientUserId;

    private String hosSickId;

    private String doctorUserId;

    private String type;

    private String otherId;

    private String visitMdtNum;

    private String orderNum;

    private String prescriptionNum;

    private String feeType;

    private String name;

    private String idCard;

    private String sex;

    private String age;

    private String mobile;

    private String departCode;

    private String departName;

    private String medicalCertificate;

    @TableField(select = false)
    private String seal;

    private String doctorSignet;

    private String img;

    private String checkStatus;

    private String checkTime;

    private String checkPharmaceutist;

    private String checkPharmaceutistId;

    private String checkContent;

    @TableField(select = false)
    private String checkReturn;

    private String totalPrice;

    private String payType;

    private String payTime;

    private String status;

    private String isPay;

    private String endTime;

    private String isCancel;

    private String kidneyStatus;

    private String liverStatus;

    private String womanStatus;

    private String allegeName;

    private String isDisable;

    private String isPush;

    private String check_timeout;

    private String manualCheckContent;

    private String manualCheckTime;

    private String manualCheckPharmaceutist;

    private String manualCheckPharmaceutistId;

    private String isCanForce;

    @TableField(select = false)
    private String manualCheckReturn;

    @TableField(exist = false)
    private String checkStatusName;

    @TableField(exist = false)
    private String hosPrescriptionDrugList;

    @TableField(exist = false)
    private String drugName;

    private String disease;

    private String syndrome;

    private String drugstoreId;

    private String medicineStatusCode;

    private Integer confirmedStatus;

    public JSONArray getTaboos() {
        JSONArray params = new JSONArray();
        if (StrUtil.isNotBlank(kidneyStatus)) {
            JSONObject map1 = new JSONObject();
            JSONObject value1 = new JSONObject();
            map1.put("code", "kidneyStatus");
            map1.put("name", "肾功能状况");
            map1.put("type", "SELECT");
            value1.put("code", kidneyStatus);
            switch (kidneyStatus) {
                case "0":
                    value1.put("name", "肾功能不全");
                    break;
                case "2":
                    value1.put("name", "严重肾功能不全");
                    break;
            }
            map1.put("value", Collections.singletonList(value1));
            params.add(map1);
        }
        if (StrUtil.isNotBlank(liverStatus)) {
            JSONObject map2 = new JSONObject();
            JSONObject value2 = new JSONObject();
            map2.put("code", "liverStatus");
            map2.put("name", "肝功能状况");
            map2.put("type", "SELECT");
            value2.put("code", liverStatus);
            switch (liverStatus) {
                case "0":
                    value2.put("name", "肝功能不全");
                    break;
                case "2":
                    value2.put("name", "严重肝功能不全");
                    break;
            }
            map2.put("value", Collections.singletonList(value2));
            params.add(map2);
        }
        if (StrUtil.isNotBlank(womanStatus)) {
            JSONObject map3 = new JSONObject();
            JSONObject value3 = new JSONObject();
            map3.put("code", "womanStatus");
            map3.put("name", "妊娠/哺乳");
            map3.put("type", "SELECT");
            value3.put("code", womanStatus);
            switch (womanStatus) {
                case "0":
                    value3.put("name", "哺乳期");
                    break;
                case "1":
                    value3.put("name", "妊娠期");
                    break;
            }
            map3.put("value", Collections.singletonList(value3));
            params.add(map3);
        }
        if (StrUtil.isNotBlank(allegeName)) {
            JSONObject map4 = new JSONObject();
            map4.put("code", "allegeName");
            map4.put("name", "过敏源名称");
            map4.put("type", "TEXT");
            JSONObject value4 = new JSONObject();
            value4.put("code", "");
            value4.put("name", allegeName);
            map4.put("value", Collections.singletonList(value4));
            params.add(map4);
        }
        return params;
    }
}