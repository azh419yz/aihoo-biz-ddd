package com.aihoo.domain.hospital.service;

import com.aihoo.domain.hospital.dto.TcmSyndromeDto;
import com.aihoo.domain.hospital.dto.TcmSyndromeListRequestDto;
import com.aihoo.domain.hospital.entity.TcmSyndrome;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TcmSyndromeService extends IService<TcmSyndrome> {

    
    List<TcmSyndromeDto> getSyndromeList(TcmSyndromeListRequestDto req);
}
