package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.vo.AreaVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.dto.AreaDto;
import com.aihoo.domain.sys.entity.Area;
import com.aihoo.domain.sys.service.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Area", description = "患者端-地区相关接口")
@RestController
@RequestMapping("/api/v2/dArea")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @GetMapping("/list")
    @Operation(summary = "省市区三级联动")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, AreaVo.class},
                            description = "省市区三级联动"
                    )
            )
    )
    public BizResult<List<AreaVo>> list() {
        return BizResult.success(toAreaVoList(areaService.provincesRelation()));
    }

    @GetMapping("/doctorProCityList")
    @Operation(summary = "省市二级联动")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, AreaVo.class},
                            description = "省市二级联动"
                    )
            )
    )
    public BizResult<List<AreaVo>> doctorProCityList() {
        return BizResult.success(toAreaVoList(areaService.doctorProCityList()));
    }

    @GetMapping("/provincesList")
    @Operation(summary = "查询省")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Area.class},
                            description = "查询省"
                    )
            )
    )
    public BizResult<List<Area>> provincesList() {
        return BizResult.success(areaService.provincesList());
    }

    @GetMapping("/cityList")
    @Operation(summary = "查询市")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Area.class},
                            description = "查询市"
                    )
            )
    )
    public BizResult<List<Area>> cityList(@RequestParam String parentAreaCode) {
        return BizResult.success(areaService.cityList(parentAreaCode));
    }

    @GetMapping("/districtList")
    @Operation(summary = "查询区/县")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Area.class},
                            description = "查询区/县"
                    )
            )
    )
    public BizResult<List<Area>> districtList(@RequestParam String parentAreaCode) {
        return BizResult.success(areaService.districtList(parentAreaCode));
    }

    private List<AreaVo> toAreaVoList(List<AreaDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }
        List<AreaVo> result = new ArrayList<>(dtos.size());
        for (AreaDto dto : dtos) {
            result.add(toAreaVo(dto));
        }
        return result;
    }

    private AreaVo toAreaVo(AreaDto dto) {
        if (dto == null) {
            return null;
        }
        AreaVo vo = new AreaVo();
        BeanUtils.copyProperties(dto, vo);
        vo.setChildren(toAreaVoList(dto.getChildren()));
        return vo;
    }
}