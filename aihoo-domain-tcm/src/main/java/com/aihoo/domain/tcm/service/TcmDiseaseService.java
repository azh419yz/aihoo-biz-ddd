package com.aihoo.domain.tcm.service;

import com.aihoo.domain.tcm.dto.TcmDiseaseDto;
import com.aihoo.domain.tcm.dto.TcmDiseaseListRequestDto;
import com.aihoo.domain.tcm.entity.TcmDisease;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 中医辨病服务接口（迁自 doctor-api 的 TcmDiseaseService）。
 */
public interface TcmDiseaseService extends IService<TcmDisease> {

    /**
     * 获取疾病列表（doctor-api: TcmDiseaseController.list）。
     */
    List<TcmDiseaseDto> getDiseaseList(TcmDiseaseListRequestDto req);
}
