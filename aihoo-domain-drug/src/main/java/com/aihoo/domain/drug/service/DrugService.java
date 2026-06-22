package com.aihoo.domain.drug.service;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.dto.SaveUpdateDrugRequestDto;
import com.aihoo.domain.drug.dto.SearchDrugRequestDto;
import com.aihoo.domain.drug.entity.Drug;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * 药品 服务接口。
 *
 * <p>patient/doctor 阶段方法：
 * <ul>
 *   <li>{@link #drugPriceList(Map)}：patient-api 药品价格分页</li>
 *   <li>{@link #getPage(PageParam, SearchDrugRequestDto)}：doctor-api 药品管理分页</li>
 * </ul>
 *
 * <p>admin 阶段方法：
 * <ul>
 *   <li>{@link #getPage(PageParam, String, String)}：admin-api 药品管理分页（支持按药房/名称/拼音首字母过滤）</li>
 *   <li>{@link #create}/{@link #update}/{@link #delete}/{@link #updateStatus}：CRUD</li>
 *   <li>{@link #drugBulkExport}：admin 阶段桩实现（Excel 导出待补）</li>
 * </ul>
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

    /**
     * 药品管理多条件分页查询（admin-api: DrugController.list）。
     *
     * <p>与 doctor-api 的 getPage 形成重载：参数类型不同（drugstoreId 可选），返回 entity 列表，
     * DrugVo（含 drugstoreName）由 controller 拼装。
     */
    PageResult<Drug> getPage(PageParam<Drug> pageParam, String drugstoreId, String name, String initial);

    /**
     * 添加药品（admin-api: DrugController.create）。
     */
    boolean create(SaveUpdateDrugRequestDto request);

    /**
     * 修改药品（admin-api: DrugController.update）。
     */
    boolean update(SaveUpdateDrugRequestDto request);

    /**
     * 删除药品（admin-api: DrugController.delete）。
     */
    boolean delete(String id);

    /**
     * 启用禁用药品（admin-api: DrugController.enableDisable）。
     */
    boolean updateStatus(String id, String status);

    /**
     * 药品批量导出（admin-api: DrugController.drugBulkExport）。
     *
     * <p>桩实现：抛 BizException，提示待实现。保留方法签名让 controller 编译通过。
     */
    void drugBulkExport(String name, String manufacturers, HttpServletRequest request, HttpServletResponse response);
}
