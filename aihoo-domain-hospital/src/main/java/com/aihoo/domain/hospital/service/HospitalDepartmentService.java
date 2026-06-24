package com.aihoo.domain.hospital.service;

import com.aihoo.domain.hospital.entity.HospitalDepartment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface HospitalDepartmentService extends IService<HospitalDepartment> {

    
    String findDepartmentNameByCode(String code);

    
    List<HospitalDepartment> findDepartCodeAllByHospitalId(String hospitalId);

    
    void deleteByDepartCodes(List<String> departCodes, String hospitalId);
}
