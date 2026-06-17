package com.aihoo.domain.patient.service;

import com.aihoo.domain.patient.entity.PatientUserAddress;
import com.aihoo.domain.patient.dto.PatientUserAddressSaveDto;
import com.aihoo.domain.patient.dto.PatientUserAddressUpdateDto;
import com.aihoo.domain.patient.dto.PatientUserAddressSelectDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PatientUserAddressService extends IService<PatientUserAddress> {

    boolean saveAddress(PatientUserAddressSaveDto request);

    boolean updateAddress(PatientUserAddressUpdateDto request);

    List<PatientUserAddress> selectAddress(PatientUserAddressSelectDto request);

    PatientUserAddress selectDefaultAddress(PatientUserAddressSelectDto request);
}
