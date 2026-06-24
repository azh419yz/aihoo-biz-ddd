package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.request.TcmDiseaseListRequest;
import com.aihoo.api.doctor.vo.TcmDiseaseVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.hospital.dto.TcmDiseaseDto;
import com.aihoo.domain.hospital.dto.TcmDiseaseListRequestDto;
import com.aihoo.domain.hospital.service.TcmDiseaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Disease", description = "医生端-中医疾病接口")
@RestController
@RequestMapping("/api/v2/disease")
@RequiredArgsConstructor
public class TcmDiseaseController {

    private final TcmDiseaseService tcmDiseaseService;

    @Operation(summary = "获取中医疾病列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, TcmDiseaseVo.class},
                            description = "获取中医疾病列表"
                    )
            )
    )
    @GetMapping("/list")
    public BizResult<List<TcmDiseaseVo>> list(@Parameter TcmDiseaseListRequest req) {
        TcmDiseaseListRequestDto dto = new TcmDiseaseListRequestDto();
        if (req != null) {
            BeanUtils.copyProperties(req, dto);
        }
        List<TcmDiseaseDto> dtos = tcmDiseaseService.getDiseaseList(dto);
        List<TcmDiseaseVo> vos = dtos.stream().map(d -> {
            TcmDiseaseVo vo = new TcmDiseaseVo();
            BeanUtils.copyProperties(d, vo);
            return vo;
        }).toList();
        return BizResult.success(vos);
    }
}
