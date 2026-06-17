package com.aihoo.domain.tcm.service;

import com.aihoo.domain.tcm.dto.TcmSyndromeDto;
import com.aihoo.domain.tcm.dto.TcmSyndromeListRequestDto;
import com.aihoo.domain.tcm.entity.TcmSyndrome;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 中医辨证服务接口（迁自 doctor-api 的 TcmSyndromeService）。
 */
public interface TcmSyndromeService extends IService<TcmSyndrome> {

    /**
     * 获取证候列表（doctor-api: TcmSyndromeController.list）。
     */
    List<TcmSyndromeDto> getSyndromeList(TcmSyndromeListRequestDto req);
}
