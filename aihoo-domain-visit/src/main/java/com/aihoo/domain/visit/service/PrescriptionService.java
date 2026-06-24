package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.PrescriptionQueryDto;
import com.aihoo.domain.visit.dto.RecentPreDto;
import com.aihoo.domain.visit.dto.SavePrescriptionDto;
import com.aihoo.domain.visit.dto.SearchRecentPreDto;
import com.aihoo.domain.visit.dto.WithdrawPrescriptionDto;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PrescriptionService extends IService<HosPrescription> {

    long countByDoctorUserId(String doctorUserId);

    
    RecentPreDto savePrescription(SavePrescriptionDto request);

    
    RecentPreDto getRecentPre(SearchRecentPreDto request);

    
    IPage<HosPrescription> getHosPrescriptionList(PrescriptionQueryDto request);

    
    RecentPreDto getRecentPreById(Long id);

    
    Boolean withdrawPrescription(WithdrawPrescriptionDto req);

    
    Boolean generateEprescription(Long prescriptionId);
}
