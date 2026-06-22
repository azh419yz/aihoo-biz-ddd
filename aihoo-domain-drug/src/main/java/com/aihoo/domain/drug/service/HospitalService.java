package com.aihoo.domain.drug.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.entity.Hospital;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * 医院 服务接口（t_hospital）。
 *
 * <p>原 aihoo-biz-service/aihoo-admin 的 HospitalService，迁至 drug 域。
 *
 * <p>跨域依赖：
 * <ul>
 *   <li>sys 域：AreaService / DictService（替代原 DiceService）— 已授权</li>
 *   <li>drug 域内：DepartmentService / HospitalDepartmentService / DepartmentMapper / HospitalDepartmentMapper</li>
 *   <li>sk：SecurityUtils / IdUtils / DateUtil / StringHandler</li>
 * </ul>
 *
 * <p>不调 doctor 域：按 user 决策，去掉原 doctorCount 统计逻辑。
 * <p>桩实现：Excel 导入导出相关方法（hospitalBulkImport/Export/hospitalDepartBulkExport）
 * 不在 admin-api 当前 controller 端点列表中，本期不迁。
 *
 * <p>API 入参沿用 Map（与原 admin 保持一致），按 CLAUDE.md 规则 1 不擅自重构。
 */
public interface HospitalService extends IService<Hospital> {

    /**
     * 医院分页列表（admin: HospitalController.list）。
     */
    PageResult<Hospital> list(Map<String, Object> map);

    /**
     * 医院详情（admin: HospitalController.controlHospital）。
     */
    com.alibaba.fastjson2.JSONObject controlHospital(String id);

    /**
     * 科室对应关系（admin: HospitalController.departmentRelation）。
     */
    com.alibaba.fastjson2.JSONArray departmentRelation();

    /**
     * 医院更新（admin: HospitalController.hospitalUpdate）。
     */
    com.alibaba.fastjson2.JSONObject hospitalUpdate(Map<String, Object> map, HttpServletRequest request);

    /**
     * 医院新增（admin: HospitalController.hospitalInsert）。
     */
    com.alibaba.fastjson2.JSONObject hospitalInsert(Map<String, Object> map, HttpServletRequest request);

    /**
     * 省市区联动（admin: HospitalController.provincesRelation）。
     */
    com.alibaba.fastjson2.JSONArray provincesRelation();

    /**
     * 医院启用禁用（admin: HospitalController.enableDisable）。
     */
    Boolean enableDisable(Map<String, Object> map, HttpServletRequest request);

    /**
     * 医院删除（admin: HospitalController.delete，软删 is_delete=1）。
     */
    Boolean updateDel(Map<String, Object> map, HttpServletRequest request);
}
