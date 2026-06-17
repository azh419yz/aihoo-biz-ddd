package com.aihoo.domain.drug.service;

import com.aihoo.common.JsonResult;
import com.aihoo.domain.drug.entity.Drug;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 药品 服务接口（迁自 patient-api 的 DrugService）。
 *
 * <p>当前仅含 patient-api 用到的 {@link #drugPriceList(Map)}，
 * doctor-api 用的方法（{@code getPage} 等）后续迁移 doctor-api 时再合并。
 */
public interface DrugService extends IService<Drug> {

    /**
     * 价格分页列表（patient-api: DrugController.drugPriceList）。
     */
    JsonResult drugPriceList(Map<String, String> map);
}
