package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.SaveUpdateDrugstoreRequestDto;
import com.aihoo.domain.hospital.dto.SearchDrugstoreRequestDto;
import com.aihoo.domain.hospital.entity.Drugstore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DrugstoreService extends IService<Drugstore> {

    
    List<Drugstore> selectDrugstoreByDrug();

    
    PageResult<Drugstore> getPage(PageParam<Drugstore> pageParam, SearchDrugstoreRequestDto request);

    
    PageResult<Drugstore> getPage(PageParam<Drugstore> pageParam, String name, String provincesCode, List<Integer> medicineStatusList);

    
    boolean create(SaveUpdateDrugstoreRequestDto request);

    
    boolean update(SaveUpdateDrugstoreRequestDto request);

    
    boolean delete(String id);

    
    boolean updateStatus(String id, String status);
}
