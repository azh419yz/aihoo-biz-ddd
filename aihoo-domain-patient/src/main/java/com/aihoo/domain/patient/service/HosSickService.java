package com.aihoo.domain.patient.service;

import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.dto.SaveUpdateHosSickDto;
import com.aihoo.domain.patient.entity.HosSick;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface HosSickService extends IService<HosSick> {

    
    long countHosSickByPatientUserId(String patientUserId);

    
    List<HosSickDto> queryHosSickByDoctorId(String doctorId);

    
    HosSickDto queryHosSickByHosSickId(String hosSickId);

    
    List<HosSickDto> patientListBySickIds(List<String> sickIds, String sickName);

    
    HosSickDto patientMsg(String id);

    
    List<HosSick> listBySickIds(List<Long> sickIds);

    
    SaveUpdateHosSickDto validateRequest(SaveUpdateHosSickDto request);

    
    int removeHosSick(String hosSickId);

    
    HosSickDto saveHosSick(SaveUpdateHosSickDto request);

    
    HosSickDto updateHosSick(SaveUpdateHosSickDto request);

    
    void setImUserSig(String hosSickId, String imUserId, String userSig);

}