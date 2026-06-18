package com.aihoo.domain.patient.service;

import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.dto.SaveUpdateHosSickDto;
import com.aihoo.domain.patient.entity.HosSick;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 就诊人 service（迁自 patient-api 的 HosSickService）。
 *
 * <p>2026-06-18 拆解循环依赖：
 * <ul>
 *   <li>{@code queryHosSickByDoctorId} 不再填 status/imGroupId（visit 域查询上移到 api 层）</li>
 *   <li>{@code queryHosSickByHosSickId} 不再填 visits（visit+prescription+doctor 聚合上移到 api 层）</li>
 *   <li>{@code patientListBySickIds} 替代 {@code patientListByDoctorId}（visit+revisit sickId 取并集由 api 层完成）</li>
 * </ul>
 */
public interface HosSickService extends IService<HosSick> {

    /**
     * 统计当前患者下的就诊人数量（patient-api: PatientUserController.orderCount/visitCount 聚合用）。
     */
    long countHosSickByPatientUserId(String patientUserId);

    /**
     * 根据医生 ID 查询当前登录患者的就诊人列表。
     * 返回基础 DTO（不含 status/imGroupId，由 api 层注入 visit 域最新问诊状态）。
     */
    List<HosSickDto> queryHosSickByDoctorId(String doctorId);

    /**
     * 根据就诊人 ID 查询就诊人详情。
     * 返回基础 DTO（不含 visits，由 api 层注入 visit+prescription+doctor 聚合）。
     */
    HosSickDto queryHosSickByHosSickId(String hosSickId);

    /**
     * 根据就诊人 ID 列表模糊查询就诊人列表（doctor-api: PatientV2Controller.patientList 用）。
     * 替代原 patientListByDoctorId(doctorId, sickName)：sickId 集合由 api 层调
     * HosVisitService.listSickIdsByDoctorUserId + HosRevisitService.listSickIdsByDoctorUserId 取并集后传入。
     */
    List<HosSickDto> patientListBySickIds(List<String> sickIds, String sickName);

    /**
     * 就诊人详情（doctor-api: PatientV2Controller.patientMsg；只 4 字段：id/name/sex/age）。
     */
    HosSickDto patientMsg(String id);

    /**
     * 根据就诊人 ID 列表批量查询（doctor-api: DoctorDirectoryV2Controller 通讯录用）。
     */
    List<HosSick> listBySickIds(List<Long> sickIds);

    /**
     * 实名认证就诊人（按身份证回填性别、生日）。
     */
    SaveUpdateHosSickDto validateRequest(SaveUpdateHosSickDto request);

    /**
     * 删除就诊人。
     */
    int removeHosSick(String hosSickId);

    /**
     * 新增就诊人。
     */
    HosSickDto saveHosSick(SaveUpdateHosSickDto request);

    /**
     * 修改就诊人。
     */
    HosSickDto updateHosSick(SaveUpdateHosSickDto request);

    /**
     * 写入 IM 用户签名。
     */
    void setImUserSig(String hosSickId, String imUserId, String userSig);
}