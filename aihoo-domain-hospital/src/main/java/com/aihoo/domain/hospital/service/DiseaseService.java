package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.entity.Disease;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface DiseaseService extends IService<Disease> {

    
    PageResult<Disease> diseaseList(Map<String, Object> map);

    
    boolean addDisease(Map<String, Object> map);

    
    boolean updateDisease(Map<String, Object> map);
}
