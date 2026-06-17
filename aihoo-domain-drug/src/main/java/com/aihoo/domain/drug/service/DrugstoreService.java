package com.aihoo.domain.drug.service;

import com.aihoo.domain.drug.entity.Drugstore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 药房 服务接口（迁自 patient-api 的 DrugstoreServiceImpl）。
 *
 * <p>当前仅含 patient-api 用到的 {@link #selectDrugstoreByDrug()}，
 * admin/doctor 用的方法（分页等）后续迁移 admin/doctor 时再合并。
 */
public interface DrugstoreService extends IService<Drugstore> {

    /**
     * 根据药材查询药房（patient-api: DrugstoreV2Controller.selectDrugstoreByDrug）。
     *
     * <p>原实现为空（{@code return List.of()}），迁移保持一致。
     */
    List<Drugstore> selectDrugstoreByDrug();
}
