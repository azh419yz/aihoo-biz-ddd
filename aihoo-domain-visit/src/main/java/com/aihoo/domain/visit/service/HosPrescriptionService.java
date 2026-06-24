package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.ConfirmedPrescriptionDto;
import com.aihoo.domain.visit.dto.HosPrescriptionInnerDto;
import com.aihoo.domain.visit.dto.PrescriptionSelectDto;
import com.aihoo.domain.visit.dto.RecentPreDto;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface HosPrescriptionService extends IService<HosPrescription> {

    
    List<HosPrescription> listByVisitMdtNum(String visitMdtNum);

    
    IPage<HosPrescription> getHosPrescriptionList(PrescriptionSelectDto request);

    
    RecentPreDto getRecentPreById(Long id, String toProvince);

    
    Boolean confirmed(ConfirmedPrescriptionDto req);

    
    HosPrescriptionInnerDto getPrescriptionInnerVo(String id);
}