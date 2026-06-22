package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.request.SaveUpdateDrugstoreRequest;
import com.aihoo.api.admin.request.SearchDrugstoreRequest;
import com.aihoo.api.admin.vo.DrugstoreVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.dto.SaveUpdateDrugstoreRequestDto;
import com.aihoo.domain.drug.entity.Drugstore;
import com.aihoo.domain.drug.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.drug.entity.DrugstoreProvinceRel;
import com.aihoo.domain.drug.service.DrugstoreMedicineStatusRelService;
import com.aihoo.domain.drug.service.DrugstoreProvinceRelService;
import com.aihoo.domain.drug.service.DrugstoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Drugstore", description = "运营端-药房相关接口")
@RestController
@RequestMapping("/api/v1/drugstore")
@RequiredArgsConstructor
public class DrugstoreController {

    private final DrugstoreService drugstoreService;
    private final DrugstoreProvinceRelService provinceRelService;
    private final DrugstoreMedicineStatusRelService medicineStatusRelService;

    /**
     * 带条件分页查询药房列表
     */
    @GetMapping("/list")
    @Operation(summary = "药房列表")
    public BizResult<PageResult<DrugstoreVo>> list(@ParameterObject PageParam<Drugstore> pageParam,
                                                   @ParameterObject SearchDrugstoreRequest request) {
        PageResult<Drugstore> page = drugstoreService.getPage(pageParam,
                request == null ? null : request.getName(),
                request == null ? null : request.getProvincesCode(),
                request == null ? null : request.getMedicineStatusList());
        return BizResult.success(toDrugstoreVoPage(page));
    }

    /**
     * 创建药房信息
     */
    @PostMapping("/insert")
    @Operation(summary = "创建药房")
    public BizResult<Void> createDrugstore(@Validated(SaveUpdateDrugstoreRequest.Save.class) @RequestBody SaveUpdateDrugstoreRequest request) {
        SaveUpdateDrugstoreRequestDto dto = new SaveUpdateDrugstoreRequestDto();
        BeanUtils.copyProperties(request, dto);
        boolean result = drugstoreService.create(dto);
        return result ? BizResult.success("创建成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "创建失败");
    }

    /**
     * 修改药房信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改药房")
    public BizResult<Void> updateDrugstore(@Validated(SaveUpdateDrugstoreRequest.Update.class) @RequestBody SaveUpdateDrugstoreRequest request) {
        SaveUpdateDrugstoreRequestDto dto = new SaveUpdateDrugstoreRequestDto();
        BeanUtils.copyProperties(request, dto);
        boolean result = drugstoreService.update(dto);
        return result ? BizResult.success("修改成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "修改失败");
    }

    /**
     * 删除药房信息
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除药房")
    public BizResult<Void> deleteDrugstore(@RequestParam String id) {
        boolean result = drugstoreService.delete(id);
        return result ? BizResult.success("删除成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "删除失败");
    }

    /**
     * 启用禁用药房
     */
    @PostMapping("/enableDisable")
    @Operation(summary = "启用禁用药房")
    public BizResult<Void> enableDisable(@RequestBody SaveUpdateDrugstoreRequest request) {
        boolean result = drugstoreService.updateStatus(request.getId(), request.getStatus());
        return result ? BizResult.success("操作成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "操作失败");
    }

    private PageResult<DrugstoreVo> toDrugstoreVoPage(PageResult<Drugstore> page) {
        if (page == null || page.getData() == null || page.getData().isEmpty()) {
            return new PageResult<>();
        }
        List<String> drugstoreIds = page.getData().stream()
                .map(Drugstore::getId)
                .toList();

        Map<String, List<Integer>> medicineStatusMap = medicineStatusRelService.list(
                        new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                                .in(DrugstoreMedicineStatusRel::getDrugstoreId, drugstoreIds))
                .stream()
                .collect(Collectors.groupingBy(
                        DrugstoreMedicineStatusRel::getDrugstoreId,
                        Collectors.mapping(DrugstoreMedicineStatusRel::getMedicineStatusCode, Collectors.toList())));

        Map<String, List<String>> provinceCodeMap = provinceRelService.list(
                        new LambdaQueryWrapper<DrugstoreProvinceRel>()
                                .in(DrugstoreProvinceRel::getDrugstoreId, drugstoreIds))
                .stream()
                .collect(Collectors.groupingBy(
                        DrugstoreProvinceRel::getDrugstoreId,
                        Collectors.mapping(DrugstoreProvinceRel::getProvinceCode, Collectors.toList())));

        List<DrugstoreVo> voList = new ArrayList<>();
        for (Drugstore drugstore : page.getData()) {
            DrugstoreVo vo = new DrugstoreVo();
            BeanUtils.copyProperties(drugstore, vo);
            vo.setProvinceList(provinceCodeMap.get(drugstore.getId()));
            vo.setMedicineStatusList(medicineStatusMap.get(drugstore.getId()));
            voList.add(vo);
        }
        return new PageResult<>(voList, page.getCount());
    }
}
