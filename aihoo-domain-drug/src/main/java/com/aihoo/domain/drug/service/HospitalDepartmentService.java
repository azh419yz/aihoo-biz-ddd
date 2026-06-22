package com.aihoo.domain.drug.service;

import com.aihoo.domain.drug.entity.HospitalDepartment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 医院-科室关联 服务接口（t_hospital_department）。
 */
public interface HospitalDepartmentService extends IService<HospitalDepartment> {

    /**
     * 根据科室 code 查询科室 name（迁自 aihoo-biz-service/aihoo-admin 的 DepartmentService.findDepartmentNameByCode）。
     */
    String findDepartmentNameByCode(String code);

    /**
     * 查询医院下所有科室关联（按 hospitalId）。
     */
    List<HospitalDepartment> findDepartCodeAllByHospitalId(String hospitalId);

    /**
     * 按 departCode 列表删除医院-科室关联。
     */
    void deleteByDepartCodes(List<String> departCodes, String hospitalId);
}
