package com.aihoo.api.patient.controller;


import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.sys.oss.OssComponent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "FileV2", description = "患者端-文件相关接口")
@RestController
@RequestMapping("/api/v2/file")
@RequiredArgsConstructor
public class FileController {

    private final OssComponent ossComponent;

    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, String.class},
                            description = "文件上传"
                    )
            )
    )
    public BizResult<String> upload(MultipartFile file) {

        try {
            String url = ossComponent.uploadFile(file, "patient_aihoo");
            return BizResult.success("上传成功", url);
        } catch (Exception e) {
            return BizResult.fail(BizResultCode.OSS_UPLOAD_ERROR);
        }
    }

}
