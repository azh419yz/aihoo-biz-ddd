package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.dto.TBaseDto;
import com.aihoo.domain.sys.entity.TBase;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface TBaseService extends IService<TBase> {

    List<TBaseDto> pageList();

    boolean addCommonWords(Map<String, Object> map);

    boolean updateCommonWords(Map<String, Object> map);
}
