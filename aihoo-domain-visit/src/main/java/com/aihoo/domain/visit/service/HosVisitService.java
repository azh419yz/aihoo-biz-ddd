package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.HosVisitBaseInfoVo;
import com.aihoo.domain.visit.dto.HosVisitCreateRequest;
import com.aihoo.domain.visit.dto.HosVisitHealthInfoVo;
import com.aihoo.domain.visit.dto.HosVisitInfoRequest;
import com.aihoo.domain.visit.entity.HosVisit;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 在线问诊信息表 服务接口（迁自 patient-api 的 HosVisitService，保留全部 19 个方法）。
 */
public interface HosVisitService extends IService<HosVisit> {

    JSONArray patientList(Map<String, Object> map);

    HosVisit createOrder(HosVisitCreateRequest request);

    void hosVisitPay(String id);

    void addHosSick(String id, String hosSickId);

    void submitInfo(String id);

    HosVisit latestHosVisit(String hosSickId, String doctorId);

    long countHostVisitByDoctor(String doctorId);

    void addHealthInfo(HosVisitInfoRequest request);

    void addBaseInfo(HosVisitInfoRequest request);

    void updateBaseInfo(HosVisitInfoRequest request);

    HosVisitHealthInfoVo getHealthInfo(String hosVisitId);

    HosVisitBaseInfoVo getBaseInfo(String hosVisitId);

    long countHosVisitByPatientUserId(String patientUserId);
}