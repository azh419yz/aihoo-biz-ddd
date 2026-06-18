package com.aihoo.domain.doctor.dto;

import lombok.Data;

import java.util.List;

/**
 * 医生排班时间 VO（迁自 aihoo-biz-service/aihoo-admin 的 DoctorSetTimeVo）。
 */
@Data
public class DoctorSetTimeVo {
    private String weekCode;
    private List<SetTime> setTimes;

    @Data
    public static class SetTime {
        private String startTime;
        private String endTime;
    }
}