package com.aihoo.domain.hospital.service;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.SaveUpdateDrugRequestDto;
import com.aihoo.domain.hospital.dto.SearchDrugRequestDto;
import com.aihoo.domain.hospital.entity.Drug;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface DrugService extends IService<Drug> {

    
    JsonResult drugPriceList(Map<String, String> map);

    
    PageResult<Drug> getPage(PageParam<Drug> pageParam, SearchDrugRequestDto request);

    
    boolean create(SaveUpdateDrugRequestDto request);

    
    boolean update(SaveUpdateDrugRequestDto request);

    
    boolean delete(String id);

    
    boolean updateStatus(String id, String status);

    
    void drugBulkExport(String name, String manufacturers, HttpServletRequest request, HttpServletResponse response);
}
