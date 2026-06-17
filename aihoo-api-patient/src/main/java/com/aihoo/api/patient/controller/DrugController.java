package com.aihoo.api.patient.controller;

import com.aihoo.common.JsonResult;
import com.aihoo.domain.drug.service.DrugService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 药品 controller（患者端，迁自 patient-api 的 DrugController）。
 *
 * @author carl
 * @since 2020-09-27
 */
@RestController
public class DrugController {
    private final DrugService drugService;

    public DrugController(DrugService drugService) {
        this.drugService = drugService;
    }

    /**
     * 搜索结果页
     */
    @ResponseBody
    @PostMapping("/drugPriceList")
    public JsonResult drugPriceList(@RequestBody Map<String, String> map) {
        try {
            return drugService.drugPriceList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error();
    }
}
