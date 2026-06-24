package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.request.SearchDrugstoreRequest;
import com.aihoo.api.doctor.vo.DrugstoreVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.SearchDrugstoreRequestDto;
import com.aihoo.domain.hospital.entity.Drugstore;
import com.aihoo.domain.hospital.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.hospital.entity.DrugstoreProvinceRel;
import com.aihoo.domain.hospital.service.DrugstoreMedicineStatusRelService;
import com.aihoo.domain.hospital.service.DrugstoreProvinceRelService;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Drugstore", description = "医生端-药店相关接口")
@RestController
@RequestMapping("/api/v2/drugstore")
@RequiredArgsConstructor
public class DrugstoreController {

    private final DrugstoreService drugstoreService;
    private final DrugstoreProvinceRelService provinceRelService;
    private final DrugstoreMedicineStatusRelService medicineStatusRelService;

    @GetMapping("/list")
    @Operation(summary = "药房列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PageResult.class},
                            description = "药房列表"
                    )
            )
    )
    public BizResult<PageResult<DrugstoreVo>> list(@Parameter PageParam<Drugstore> pageParam,
                                                   @Parameter SearchDrugstoreRequest request) {
        SearchDrugstoreRequestDto dto = new SearchDrugstoreRequestDto();
        BeanUtils.copyProperties(request, dto);
        PageResult<Drugstore> drugstorePage = drugstoreService.getPage(pageParam, dto);
        PageResult<DrugstoreVo> voPage = new PageResult<>();
        if (drugstorePage.getData() != null) {
            List<Drugstore> records = drugstorePage.getData();
            List<String> drugstoreIdList = records.stream().map(Drugstore::getId).toList();

            Map<String, List<Integer>> medicineStatusCodeMap = CollectionUtils.isEmpty(drugstoreIdList)
                    ? Map.of()
                    : medicineStatusRelService.list(new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                            .in(DrugstoreMedicineStatusRel::getDrugstoreId, drugstoreIdList))
                    .stream()
                    .collect(Collectors.groupingBy(DrugstoreMedicineStatusRel::getDrugstoreId,
                            Collectors.mapping(DrugstoreMedicineStatusRel::getMedicineStatusCode, Collectors.toList())));

            Map<String, List<String>> provinceCodeMap = CollectionUtils.isEmpty(drugstoreIdList)
                    ? Map.of()
                    : provinceRelService.list(new LambdaQueryWrapper<DrugstoreProvinceRel>()
                            .in(DrugstoreProvinceRel::getDrugstoreId, drugstoreIdList))
                    .stream()
                    .collect(Collectors.groupingBy(DrugstoreProvinceRel::getDrugstoreId,
                            Collectors.mapping(DrugstoreProvinceRel::getProvinceCode, Collectors.toList())));

            List<DrugstoreVo> voList = new ArrayList<>();
            for (Drugstore drugstore : records) {
                DrugstoreVo vo = new DrugstoreVo();
                BeanUtils.copyProperties(drugstore, vo);
                vo.setProvinceList(provinceCodeMap.get(drugstore.getId()));
                vo.setMedicineStatusList(medicineStatusCodeMap.get(drugstore.getId()));
                voList.add(vo);
            }
            voPage.setData(voList);
        }
        voPage.setCount(drugstorePage.getCount());
        return BizResult.success(voPage);
    }
}
