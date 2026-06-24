package com.aihoo.api.patient.controller;

import com.aihoo.common.JsonResult;
import com.aihoo.domain.hospital.service.DrugService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Drug", description = "Drug相关接口")
@RestController
public class DrugController {
    private final DrugService drugService;

    public DrugController(DrugService drugService) {
        this.drugService = drugService;
    }

    
    @Operation(summary = "drugPriceList")
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
