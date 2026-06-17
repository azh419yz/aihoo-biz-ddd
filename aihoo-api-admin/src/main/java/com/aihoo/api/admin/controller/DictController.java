package com.aihoo.api.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.aihoo.common.JsonResult;
import com.aihoo.domain.sys.service.DiceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/v1/dict")
@RestController
public class DictController {
    @Resource
    private DiceService diceService;

    /**
     * 疾病列表
     *
     * @param map 入参
     * @return {}
     */
    @PostMapping("/listByType")
    public JsonResult listByType(@RequestBody Map<String, String> map) {
        try {
            String type = map.get("type");
            if (StrUtil.isBlank(type)) {
                return JsonResult.error("type不能为空");
            }
            return diceService.listByType(type);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("疾病列表查询出错");
        }
    }
}
