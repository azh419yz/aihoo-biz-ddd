package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.*;
import com.aihoo.domain.visit.entity.HosVisit;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface HosVisitService extends IService<HosVisit> {

    JSONArray patientList(Map<String, Object> map);

    HosVisit createOrder(HosVisitCreateDto request);

    void hosVisitPay(String id);

    void addHosSick(String id, String hosSickId);

    void submitInfo(String id);

    HosVisit latestHosVisit(String hosSickId, String doctorId);

    long countHostVisitByDoctor(String doctorId);

    
    Long countByDoctorUserId(String doctorUserId);

    void addHealthInfo(HosVisitInfoDto request);

    void addBaseInfo(HosVisitInfoDto request);

    void updateBaseInfo(HosVisitInfoDto request);

    HosVisitHealthInfoDto getHealthInfo(String hosVisitId);

    HosVisitBaseInfoRespDto getBaseInfo(String hosVisitId);

    long countHosVisitByPatientUserId(String patientUserId);

    
    HosOrderDto visitData(String id);

    
    List<HosVisit> listVisitsByHosSickId(String hosSickId);

    
    List<String> listSickIdsByDoctorUserId(String doctorId);

    
    void updateMsg(String orderNum, String msg);
}