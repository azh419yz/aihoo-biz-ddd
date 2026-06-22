package com.aihoo.domain.drug.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.dto.SaveUpdateDrugstoreRequestDto;
import com.aihoo.domain.drug.dto.SearchDrugstoreRequestDto;
import com.aihoo.domain.drug.entity.Drugstore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 药房 服务接口。
 *
 * <p>patient/doctor 阶段方法：
 * <ul>
 *   <li>{@link #selectDrugstoreByDrug}：patient-api 药材→药房</li>
 *   <li>{@link #getPage(PageParam, SearchDrugstoreRequestDto)}：doctor-api 药房管理分页</li>
 * </ul>
 *
 * <p>admin 阶段方法：
 * <ul>
 *   <li>{@link #getPage(PageParam, String, String, List)}：admin-api 药房管理分页</li>
 *   <li>{@link #create}/{@link #update}/{@link #delete}/{@link #updateStatus}：CRUD</li>
 * </ul>
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

    /**
     * 带条件分页查询药房列表（admin-api: DrugstoreController.list）。
     *
     * <p>与 doctor-api 的 getPage 形成重载：参数类型不同（原始参数，非 DTO），便于 controller 转 vo。
     */
    PageResult<Drugstore> getPage(PageParam<Drugstore> pageParam, String name, String provincesCode, List<Integer> medicineStatusList);

    /**
     * 创建药房（admin-api: DrugstoreController.createDrugstore）。
     */
    boolean create(SaveUpdateDrugstoreRequestDto request);

    /**
     * 修改药房（admin-api: DrugstoreController.updateDrugstore）。
     */
    boolean update(SaveUpdateDrugstoreRequestDto request);

    /**
     * 删除药房（admin-api: DrugstoreController.deleteDrugstore）。
     */
    boolean delete(String id);

    /**
     * 启用禁用药房（admin-api: DrugstoreController.enableDisable）。
     */
    boolean updateStatus(String id, String status);
}
