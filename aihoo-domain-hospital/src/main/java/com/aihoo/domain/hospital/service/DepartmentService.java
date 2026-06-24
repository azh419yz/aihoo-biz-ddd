package com.aihoo.domain.hospital.service;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface DepartmentService extends IService<Department> {

    
    String findDepartmentNameByCode(String code);

    
    PageResult<Department> departmentsList(Map<String, Object> map);

    
    JsonResult add(Map<String, Object> map);

    
    JsonResult departmentUpdate(Map<String, Object> map);

    
    void departmentEnableDisable(Map<String, Object> map);
}
