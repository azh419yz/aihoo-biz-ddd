package com.aihoo.domain.drug.service;

import com.aihoo.domain.drug.entity.HospitalDepartment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Classname HospitalDepartmentService
 * @Description hf
 * @Date 2020/9/18 17:02
 * @Created by ad
 */
public interface HospitalDepartmentService extends IService<HospitalDepartment> {

    /**
     * 根据科室 code 查询科室 name（迁自 aihoo-biz-service/aihoo-admin 的 DepartmentService.findDepartmentNameByCode）。
     */
    String findDepartmentNameByCode(String code);
}
