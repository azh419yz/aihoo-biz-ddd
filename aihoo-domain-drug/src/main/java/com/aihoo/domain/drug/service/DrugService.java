package com.aihoo.domain.drug.service;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.dto.SearchDrugRequestDto;
import com.aihoo.domain.drug.entity.Drug;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 药品 服务接口（迁自 patient-api 的 DrugService）。
 *
 * <p>当前含 patient-api 用到的 {@link #drugPriceList(Map)} + doctor-api 用到的 {@link #getPage(PageParam, SearchDrugRequestDto)}。
 */
public interface DrugService extends IService<Drug> {

    /**
     * 价格分页列表（patient-api: DrugController.drugPriceList）。
     */
    JsonResult drugPriceList(Map<String, String> map);

    /**
     * 药品管理多条件分页查询（doctor-api: DrugController.list）。
     */
    PageResult<Drug> getPage(PageParam<Drug> pageParam, SearchDrugRequestDto request);
}
