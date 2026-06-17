package com.aihoo.api.doctor.controller;

import com.aihoo.common.BizResult;
import com.aihoo.domain.patient.dto.HosSickVo;
import com.aihoo.domain.visit.service.HosSickService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/6 14:15
 */
@Tag(name = "HosSick", description = "医生端-获取患者详情")
@RestController
@RequestMapping("/api/v2/sick")
@RequiredArgsConstructor
public class HosSickV2Controller {

    private final HosSickService hosSickService;

    @GetMapping("/{id}")
    public BizResult<HosSickVo> findSickById(@PathVariable Long id) {
        if (id == null || id == 0) {
            return BizResult.fail(500, "参数错误");
        }
        return BizResult.success(hosSickService.findHosSickViewById(id));
    }
}
