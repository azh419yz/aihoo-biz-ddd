package com.aihoo.api.admin.controller;


import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.oss.OssComponent;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.aihoo.util.SecurityUtils.getLoginUser;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController extends BaseController {
    private final OssComponent ossComponent;

    @PostMapping("/upload")
    public JsonResult upload(MultipartFile file) {
        if (null == getLoginUser()) {
            return error("请先登录");
        }
        if (null == file || file.getSize() == 0) {
            return JsonResult.error("请选择文件");
        }
        try {
            String url = ossComponent.uploadFile(file, "admin_aihoo");
            JSONObject data = new JSONObject();
            data.put("src", url);
            return JsonResult.ok(0, "操作成功").put("data", data);
        } catch (Exception e) {
            return JsonResult.error("文件上传失败");
        }
    }

}
