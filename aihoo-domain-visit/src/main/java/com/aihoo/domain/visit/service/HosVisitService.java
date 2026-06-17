package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.HosOrderDto;
import com.aihoo.domain.visit.dto.HosVisitBaseInfoVo;
import com.aihoo.domain.visit.dto.HosVisitCreateRequest;
import com.aihoo.domain.visit.dto.HosVisitHealthInfoVo;
import com.aihoo.domain.visit.dto.HosVisitInfoRequest;
import com.aihoo.domain.visit.entity.HosVisit;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 在线问诊信息表 服务接口（迁自 patient-api 的 HosVisitService + doctor-api 的 HosVisitService）。
 */
public interface HosVisitService extends IService<HosVisit> {

    JSONArray patientList(Map<String, Object> map);

    HosVisit createOrder(HosVisitCreateRequest request);

    void hosVisitPay(String id);

    void addHosSick(String id, String hosSickId);

    void submitInfo(String id);

    HosVisit latestHosVisit(String hosSickId, String doctorId);

    long countHostVisitByDoctor(String doctorId);

    /**
     * 统计医生患者数（doctor-api: DoctorUserV2Controller.phoneLogin/loginUser 用，countHostVisitByDoctor 的别名）。
     */
    Long countByDoctorUserId(String doctorUserId);

    void addHealthInfo(HosVisitInfoRequest request);

    void addBaseInfo(HosVisitInfoRequest request);

    void updateBaseInfo(HosVisitInfoRequest request);

    HosVisitHealthInfoVo getHealthInfo(String hosVisitId);

    HosVisitBaseInfoVo getBaseInfo(String hosVisitId);

    long countHosVisitByPatientUserId(String patientUserId);

    /**
     * 问诊详情（doctor-api: HosVisitV2Controller.visitData）。
     *
     * <p>id 支持 orderNum（V 开头）或 hosVisit.id；老 doctor-api 中含倒计时计算与按钮 JSON 生成（依赖 TBase，DDD 阶段简化）。
     */
    HosOrderDto visitData(String id);
}