package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.dto.AreaDto;
import com.aihoo.domain.sys.entity.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface AreaService extends IService<Area> {

    String getAreaByCode(String code);

    
    List<AreaDto> provincesRelation();

    
    List<AreaDto> doctorProCityList();

    
    List<AreaDto> l3List();

    
    List<AreaDto> l2List();

    List<Area> provincesList();

    List<Area> cityList(String parentAreaCode);

    List<Area> districtList(String parentAreaCode);
}