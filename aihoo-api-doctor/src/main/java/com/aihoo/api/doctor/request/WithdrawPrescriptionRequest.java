package com.aihoo.api.doctor.request;

import lombok.Data;

/**
 * 医生端-撤回处方请求。
 */
@Data
public class WithdrawPrescriptionRequest {

    private String prescriptionId;
}
