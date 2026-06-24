package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.request.SearchDrugRequest;
import com.aihoo.api.doctor.vo.DrugVo;
import com.aihoo.common.BaseController;
import com.aihoo.common.BizResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.SearchDrugRequestDto;
import com.aihoo.domain.hospital.entity.Drug;
import com.aihoo.domain.hospital.service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Drug", description = "医生端-药品相关接口")
@RestController
@RequestMapping("/api/v2/drug")
@RequiredArgsConstructor
public class DrugController extends BaseController {

    private final DrugService drugService;

    @Operation(summary = "药品列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PageResult.class},
                            description = "药品列表"
                    )
            )
    )
    @GetMapping("/list")
    public BizResult<PageResult<DrugVo>> list(@ParameterObject PageParam<Drug> pageParam,
                                              @ParameterObject SearchDrugRequest request) {
        SearchDrugRequestDto dto = new SearchDrugRequestDto();
        BeanUtils.copyProperties(request, dto);
        PageResult<Drug> drugPage = drugService.getPage(pageParam, dto);
        PageResult<DrugVo> voPage = new PageResult<>();
        if (drugPage.getData() != null) {
            List<DrugVo> voList = new ArrayList<>();
            for (Drug drug : drugPage.getData()) {
                DrugVo vo = new DrugVo();
                BeanUtils.copyProperties(drug, vo);
                voList.add(vo);
            }
            voPage.setData(voList);
        }
        voPage.setCount(drugPage.getCount());
        return BizResult.success(voPage);
    }
}
