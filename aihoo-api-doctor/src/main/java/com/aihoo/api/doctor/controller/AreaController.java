package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.vo.AreaVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.dto.AreaDto;
import com.aihoo.domain.sys.entity.Area;
import com.aihoo.domain.sys.service.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Area", description = "医生端-地区相关接口")
@RestController
@RequestMapping("/api/v2/area")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @GetMapping("/l3List")
    @Operation(summary = "省市区三级联动")
    public BizResult<List<AreaVo>> l3List() {
        return BizResult.success(toVoList(areaService.l3List()));
    }

    @GetMapping("/l2List")
    @Operation(summary = "省市二级联动")
    public BizResult<List<AreaVo>> l2List() {
        return BizResult.success(toVoList(areaService.l2List()));
    }

    @GetMapping("/provincesList")
    @Operation(summary = "查询省")
    public BizResult<List<Area>> provincesList() {
        return BizResult.success(areaService.provincesList());
    }

    @GetMapping("/cityList")
    @Operation(summary = "查询市")
    public BizResult<List<Area>> cityList(@RequestParam String parentAreaCode) {
        return BizResult.success(areaService.cityList(parentAreaCode));
    }

    @GetMapping("/districtList")
    @Operation(summary = "查询区/县")
    public BizResult<List<Area>> districtList(@RequestParam String parentAreaCode) {
        return BizResult.success(areaService.districtList(parentAreaCode));
    }

    private List<AreaVo> toVoList(List<AreaDto> dtoList) {
        return dtoList.stream().map(dto -> {
            AreaVo vo = new AreaVo();
            BeanUtils.copyProperties(dto, vo);
            if (dto.getChildren() != null) {
                vo.setChildren(toVoList(dto.getChildren()));
            }
            return vo;
        }).collect(Collectors.toList());
    }
}
