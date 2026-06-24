package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.dto.DictTypeItemDto;
import com.aihoo.domain.sys.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface DictService extends IService<Dict> {

    List<DictTypeItemDto> getDoctorType(String type);

    List<Map<String, String>> listByType(String type);

    
    String getDoctorNameByTypeAndCode(String type, String code);
}
