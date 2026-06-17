package com.aihoo.api.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.service.DictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Dict", description = "字典查询")
@RestController
@RequestMapping("/api/v1/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @PostMapping("/listByType")
    public BizResult<List<Map<String, String>>> listByType(@RequestBody Map<String, String> map) {
        String type = map.get("type");
        if (StrUtil.isBlank(type)) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "type不能为空");
        }
        return BizResult.success(dictService.listByType(type));
    }
}
