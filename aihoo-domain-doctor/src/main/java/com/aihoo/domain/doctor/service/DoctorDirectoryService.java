package com.aihoo.domain.doctor.service;

import com.aihoo.domain.doctor.dto.DoctorDirectoryDto;
import com.aihoo.domain.doctor.entity.DoctorDirectory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DoctorDirectoryService extends IService<DoctorDirectory> {

    /**
     * 查询医生通讯录（迁自 doctor-api: DoctorDirectoryServiceImpl.findDoctorDirectoryList）。
     */
    List<DoctorDirectoryDto> findDoctorDirectoryList(String sickName);
}
