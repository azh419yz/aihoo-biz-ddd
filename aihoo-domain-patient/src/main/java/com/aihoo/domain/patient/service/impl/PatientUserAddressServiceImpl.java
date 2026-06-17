package com.aihoo.domain.patient.service.impl;

import com.aihoo.domain.patient.dto.PatientUserAddressSaveDto;
import com.aihoo.domain.patient.dto.PatientUserAddressSelectDto;
import com.aihoo.domain.patient.dto.PatientUserAddressUpdateDto;
import com.aihoo.domain.patient.entity.PatientUserAddress;
import com.aihoo.domain.patient.mapper.PatientUserAddressMapper;
import com.aihoo.domain.patient.service.PatientUserAddressService;
import com.aihoo.security.AuthUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientUserAddressServiceImpl extends ServiceImpl<PatientUserAddressMapper, PatientUserAddress> implements PatientUserAddressService {
    @Override
    public boolean saveAddress(PatientUserAddressSaveDto request) {
        PatientUserAddress address = new PatientUserAddress();
        BeanUtils.copyProperties(request, address);
        address.setPatientUserId(Long.valueOf(AuthUtil.getLoginUserId()));
        return save(address);
    }

    @Override
    public boolean updateAddress(PatientUserAddressUpdateDto request) {
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            update(new LambdaUpdateWrapper<PatientUserAddress>()
                    .set(PatientUserAddress::getIsDefault, 0)
                    .eq(PatientUserAddress::getPatientUserId, AuthUtil.getLoginUserId()));
        }

        return update(new LambdaUpdateWrapper<PatientUserAddress>()
                .set(StringUtils.isNotEmpty(request.getAreaCode()), PatientUserAddress::getAreaCode, request.getAreaCode())
                .set(StringUtils.isNotEmpty(request.getDetailAddress()), PatientUserAddress::getDetailAddress, request.getDetailAddress())
                .set(StringUtils.isNotEmpty(request.getAreaName()), PatientUserAddress::getAreaName, request.getAreaName())
                .set(StringUtils.isNotEmpty(request.getReceiverPhone()), PatientUserAddress::getReceiverPhone, request.getReceiverPhone())
                .set(StringUtils.isNotEmpty(request.getReceiverName()), PatientUserAddress::getReceiverName, request.getReceiverName())
                .set(request.getIsDefault() != null, PatientUserAddress::getIsDefault, request.getIsDefault())
                .eq(PatientUserAddress::getId, request.getId())
        );
    }

    @Override
    public List<PatientUserAddress> selectAddress(PatientUserAddressSelectDto request) {
        return list(new LambdaQueryWrapper<PatientUserAddress>()
                .eq(PatientUserAddress::getPatientUserId, AuthUtil.getLoginUserId())
                .orderByDesc(PatientUserAddress::getIsDefault)
                .orderByDesc(PatientUserAddress::getCreateTime));
    }

    @Override
    public PatientUserAddress selectDefaultAddress(PatientUserAddressSelectDto request) {
        List<PatientUserAddress> address = selectAddress(request);
        if (CollectionUtils.isEmpty(address)) {
            return null;
        }
        List<PatientUserAddress> defaultAddress =
                address.stream().filter(a -> a.getIsDefault().equals(1)).toList();

        if (CollectionUtils.isEmpty(defaultAddress)) {
            return address.get(0);
        } else {
            return defaultAddress.get(0);
        }
    }
}
