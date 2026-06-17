package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.SaveUpdateHosSickRequest;
import com.aihoo.api.patient.vo.HosSickVo;
import com.aihoo.api.patient.vo.HosVisitVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.dto.HosVisitDto;
import com.aihoo.domain.patient.dto.SaveUpdateHosSickDto;
import com.aihoo.domain.patient.service.HosSickService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 就诊人信息表 前端控制器（迁自 patient-api 的 HosSickV2Controller）。
 */
@Tag(name = "HosSickV2", description = "患者端-就诊人相关接口")
@RestController
@RequestMapping("/api/v2/hosSick")
@RequiredArgsConstructor
public class HosSickController {

    private final HosSickService hosSickService;

    /**
     * 查询所有就诊人信息
     */
    @GetMapping("/queryByPatientUserId")
    @Operation(summary = "查询所有就诊人信息")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "查询所有就诊人信息"
                    )
            )
    )
    public BizResult<List<HosSickVo>> queryHosSickByPatientUserId(@RequestParam(required = false) String doctorId) {
        List<HosSickDto> dtos = hosSickService.queryHosSickByDoctorId(doctorId);
        return BizResult.success(dtos.stream().map(this::toVo).toList());
    }

    /**
     * 查询单个就诊人信息
     */
    @GetMapping("/queryByHosSickId")
    @Operation(summary = "查询单个就诊人信息")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "返回患者信息"
                    )
            )
    )
    public BizResult<HosSickVo> queryHosSickByHosSickId(@ParameterObject String hosSickId) {
        return BizResult.success(toVo(hosSickService.queryHosSickByHosSickId(hosSickId)));
    }

    /**
     * 认证就诊人
     */
    @PostMapping("/check")
    @Operation(summary = "认证就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, SaveUpdateHosSickRequest.class},
                            description = "认证就诊人"
                    )
            )
    )
    public BizResult<SaveUpdateHosSickRequest> validateRequest(
            @Validated(SaveUpdateHosSickRequest.Check.class) @RequestBody SaveUpdateHosSickRequest request) {
        SaveUpdateHosSickDto dto = new SaveUpdateHosSickDto();
        BeanUtils.copyProperties(request, dto);
        SaveUpdateHosSickDto result = hosSickService.validateRequest(dto);
        SaveUpdateHosSickRequest resp = new SaveUpdateHosSickRequest();
        BeanUtils.copyProperties(result, resp);
        return BizResult.success(resp);
    }

    /**
     * 删除就诊人
     */
    @DeleteMapping("/delete/{hosSickId}")
    @Operation(summary = "删除就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Integer.class},
                            description = "删除就诊人"
                    )
            )
    )
    public BizResult<Integer> deleteHosSickById(@PathVariable String hosSickId) {
        return BizResult.success(hosSickService.removeHosSick(hosSickId));
    }

    /**
     * 增加就诊人
     */
    @PostMapping("/save")
    @Operation(summary = "增加就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "添加就诊人成功"
                    )
            )
    )
    public BizResult<HosSickVo> saveHosSick(
            @Validated(SaveUpdateHosSickRequest.Save.class) @RequestBody SaveUpdateHosSickRequest request
    ) {
        SaveUpdateHosSickDto dto = new SaveUpdateHosSickDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success("添加就诊人成功", toVo(hosSickService.saveHosSick(dto)));
    }

    /**
     * 修改就诊人
     */
    @PutMapping("/update")
    @Operation(summary = "修改就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "修改就诊人成功"
                    )
            )
    )
    public BizResult<HosSickVo> updateHosSickById(
            @Validated(SaveUpdateHosSickRequest.Update.class) @RequestBody SaveUpdateHosSickRequest request
    ) {
        SaveUpdateHosSickDto dto = new SaveUpdateHosSickDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success(toVo(hosSickService.updateHosSick(dto)));
    }

    private HosSickVo toVo(HosSickDto dto) {
        if (dto == null) return null;
        HosSickVo vo = new HosSickVo();
        BeanUtils.copyProperties(dto, vo);
        if (dto.getVisits() != null) {
            List<HosVisitVo> visitVos = dto.getVisits().stream().map(this::toVo).toList();
            vo.setVisits(visitVos);
        }
        return vo;
    }

    private HosVisitVo toVo(HosVisitDto dto) {
        if (dto == null) return null;
        HosVisitVo vo = new HosVisitVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}