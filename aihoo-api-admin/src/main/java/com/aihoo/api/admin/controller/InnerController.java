package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.controller.vo.HosPrescriptionInnerVo;
import com.aihoo.common.BaseController;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.prescription.service.HosPrescriptionService;
import com.aihoo.oss.OssComponent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Inner", description = "内部调用相关接口")
@RestController
@RequestMapping("/api/inner")
@RequiredArgsConstructor
@Slf4j
public class InnerController extends BaseController {

    private final OssComponent ossComponent;
    private final HosPrescriptionService hosPrescriptionService;

    @PostMapping(value = "/upload/oss", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件到OSS", description = "上传文件到阿里云OSS")
    public BizResult<String> uploadToOss(
            @Parameter(name = "file", description = "待上传的文件", required = true)
            @RequestPart("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("上传文件为空");
            return BizResult.fail(BizResultCode.BAD_REQUEST, "请选择要上传的文件");
        }
        String url;
        try {
            url = ossComponent.uploadFile(file, "inner_aihoo/prescription");
            return BizResult.success("文件上传成功", url);
        } catch (Exception e) {
            return BizResult.fail(BizResultCode.INTERNAL_ERROR, "文件上传失败");
        }
    }


    @GetMapping("/prescription")
    @Operation(summary = "获取处方单数据", description = "根据处方ID获取处方单详细信息")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosPrescriptionInnerVo.class},
                            description = "处方笺VO"
                    )
            )
    )
    public BizResult<HosPrescriptionInnerVo> getPrescription(
            @Parameter(name = "id", description = "处方ID", required = true)
            @RequestParam("id") String id) {
        HosPrescriptionInnerVo prescriptionDetails = (HosPrescriptionInnerVo) hosPrescriptionService.getPrescriptionInnerVo(id);
        return BizResult.success(prescriptionDetails);
    }
}
