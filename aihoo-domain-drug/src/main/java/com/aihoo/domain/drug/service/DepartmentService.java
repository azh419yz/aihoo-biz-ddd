package com.aihoo.domain.drug.service;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 基础科室 服务接口（t_department）。
 *
 * <p>原 aihoo-biz-service/aihoo-admin 的 DepartmentService，迁至 drug 域。
 *
 * <p>注意：drug 域另有 HospitalDepartmentService（t_hospital_department 表），
 * 也提供 {@code findDepartmentNameByCode} 方法。两者同名但数据源不同：
 * <ul>
 *   <li>DepartmentService.findDepartmentNameByCode → t_department（基础科室字典）</li>
 *   <li>HospitalDepartmentService.findDepartmentNameByCode → t_hospital_department（医院-科室关联）</li>
 * </ul>
 * 调用方根据需要注入对应 service。
 *
 * <p>API 入参沿用 Map（与原 admin 保持一致），按 CLAUDE.md 规则 1 不擅自重构。
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 根据科室 code 查询科室 name（t_department）。
     */
    String findDepartmentNameByCode(String code);

    /**
     * 科室分页列表（admin: DepartmentsController.list）。
     */
    PageResult<Department> departmentsList(Map<String, Object> map);

    /**
     * 科室新增（admin: DepartmentsController.add）。
     *
     * <p>桩实现：跳过 DicDepartment 医保上报（该表不在当前 DDD 表清单中，后续按需补）。
     */
    JsonResult add(Map<String, Object> map);

    /**
     * 科室更新（admin: DepartmentsController.update）。
     */
    JsonResult departmentUpdate(Map<String, Object> map);

    /**
     * 科室启用禁用（admin: DepartmentsController.departmentEnableDisable）。
     *
     * <p>桩实现：跳过 DicDepartment 医保上报。
     */
    void departmentEnableDisable(Map<String, Object> map);
}
