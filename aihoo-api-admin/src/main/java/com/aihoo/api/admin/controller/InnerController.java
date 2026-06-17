package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.vo.HosPrescriptionInnerVo;
import com.aihoo.api.admin.vo.PrescriptionDrugVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.oss.OssComponent;
import com.aihoo.domain.visit.dto.HosPrescriptionInnerDto;
import com.aihoo.domain.visit.dto.PrescriptionDrugInnerDto;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 内部调用 API（含处方笺 / OSS 上传）。
 * <p>跨域：HosPrescriptionService 在 visit 域（Rule H 授权）。
 */
@Tag(name = "Inner", description = "内部调用相关接口")
@RestController
@RequestMapping("/api/inner")
@RequiredArgsConstructor
@Slf4j
public class InnerController {

    private final OssComponent ossComponent;
    private final HosPrescriptionService hosPrescriptionService;

    @PostMapping(value = "/upload/oss", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件到OSS")
    public BizResult<String> uploadToOss(@RequestPart("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("上传文件为空");
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请选择要上传的文件");
        }
        try {
            String url = ossComponent.uploadFile(file, "inner_aihoo/prescription");
            return BizResult.success("文件上传成功", url);
        } catch (Exception e) {
            return BizResult.fail(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "文件上传失败");
        }
    }

    @GetMapping("/prescription")
    @Operation(summary = "获取处方单数据")
    public BizResult<HosPrescriptionInnerVo> getPrescription(@RequestParam("id") String id) {
        HosPrescriptionInnerDto dto = hosPrescriptionService.getPrescriptionInnerVo(id);
        return BizResult.success(toVo(dto));
    }

    private HosPrescriptionInnerVo toVo(HosPrescriptionInnerDto dto) {
        if (dto == null) return null;
        HosPrescriptionInnerVo vo = new HosPrescriptionInnerVo();
        BeanUtils.copyProperties(dto, vo);
        if (dto.getDrugVoList() != null) {
            vo.setDrugVoList(dto.getDrugVoList().stream().map(this::toVo).collect(Collectors.toList()));
        }
        return vo;
    }

    private PrescriptionDrugVo toVo(PrescriptionDrugInnerDto dto) {
        if (dto == null) return null;
        PrescriptionDrugVo vo = new PrescriptionDrugVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
