package com.aihoo.domain.hospital.service;

import com.aihoo.domain.hospital.dto.TcmDiseaseDto;
import com.aihoo.domain.hospital.dto.TcmDiseaseListRequestDto;
import com.aihoo.domain.hospital.entity.TcmDisease;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TcmDiseaseService extends IService<TcmDisease> {

    
    List<TcmDiseaseDto> getDiseaseList(TcmDiseaseListRequestDto req);
}
