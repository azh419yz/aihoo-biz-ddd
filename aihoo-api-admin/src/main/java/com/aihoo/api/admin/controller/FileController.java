package com.aihoo.api.admin.controller;

import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.oss.OssComponent;
import com.aihoo.util.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "File", description = "文件上传")
@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final OssComponent ossComponent;

    @PostMapping("/upload")
    public BizResult<Map<String, String>> upload(MultipartFile file) {
        if (SecurityUtils.getLoginUserId() == null) {
            return BizResult.fail(com.aihoo.common.BizResultCode.UNAUTHORIZED, "请先登录");
        }
        if (file == null || file.getSize() == 0) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请选择文件");
        }
        try {
            String url = ossComponent.uploadFile(file, "admin_aihoo");
            Map<String, String> data = new HashMap<>();
            data.put("src", url);
            return BizResult.success("操作成功", data);
        } catch (Exception e) {
            return BizResult.fail(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "文件上传失败");
        }
    }
}
