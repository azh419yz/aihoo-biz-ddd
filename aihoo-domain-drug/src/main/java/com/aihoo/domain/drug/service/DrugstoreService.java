package com.aihoo.domain.drug.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.dto.SearchDrugstoreRequestDto;
import com.aihoo.domain.drug.entity.Drugstore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 药房 服务接口（迁自 patient-api 的 DrugstoreServiceImpl + doctor-api 的 DrugstoreServiceImpl）。
 *
 * <p>当前含 patient-api 用到的 {@link #selectDrugstoreByDrug()} + doctor-api 用到的 {@link #getPage(PageParam, SearchDrugstoreRequestDto)}。
 */
public interface DrugstoreService extends IService<Drugstore> {

    /**
     * 根据药材查询药房（patient-api: DrugstoreV2Controller.selectDrugstoreByDrug）。
     *
     * <p>原实现为空（{@code return List.of()}），迁移保持一致。
     */
    List<Drugstore> selectDrugstoreByDrug();

    /**
     * 带条件分页查询药房列表（doctor-api: DrugstoreController.list）。
     */
    PageResult<Drugstore> getPage(PageParam<Drugstore> pageParam, SearchDrugstoreRequestDto request);
}
