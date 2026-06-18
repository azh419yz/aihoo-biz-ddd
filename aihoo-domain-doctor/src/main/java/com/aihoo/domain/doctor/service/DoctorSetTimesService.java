package com.aihoo.domain.doctor.service;

import com.aihoo.domain.doctor.dto.DoctorSetTimeVo;
import com.aihoo.domain.doctor.entity.DoctorSetTimes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DoctorSetTimesService extends IService<DoctorSetTimes> {

    /**
     * 复诊/接诊时间设置（迁自 aihoo-biz-service/aihoo-admin 的 DoctorSetTimesServiceImpl.addAcceptsTime）。
     * 先删除原数据再插入。
     */
    void addAcceptsTime(List<DoctorSetTimeVo> doctorSetTimeVos, String doctorUserId, String type);
}