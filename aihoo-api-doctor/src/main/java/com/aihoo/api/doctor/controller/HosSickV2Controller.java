package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.vo.HosSickVo;
import com.aihoo.api.doctor.vo.HosVisitVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.dto.HosVisitDto;
import com.aihoo.domain.patient.service.HosSickService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 医生端-获取患者详情（迁自 doctor-api: HosSickV2Controller）。
 *
 * <p>复用 patient 阶段的 {@link HosSickService#queryHosSickByHosSickId}（逻辑与老 doctor-api 的 findHosSickViewById 一致），
 * controller 负责 DTO → VO 转换。
 */
@Tag(name = "HosSick", description = "医生端-获取患者详情")
@RestController
@RequestMapping("/api/v2/sick")
@RequiredArgsConstructor
public class HosSickV2Controller {

    private final HosSickService hosSickService;

    @GetMapping("/{id}")
    public BizResult<HosSickVo> findSickById(@PathVariable String id) {
        if (id == null || "0".equals(id)) {
            return BizResult.fail(500, "参数错误");
        }
        HosSickDto dto = hosSickService.queryHosSickByHosSickId(id);
        return BizResult.success(convert2Vo(dto));
    }

    private HosSickVo convert2Vo(HosSickDto dto) {
        if (dto == null) {
            return null;
        }
        HosSickVo vo = new HosSickVo();
        BeanUtils.copyProperties(dto, vo);
        if (dto.getVisits() != null) {
            List<HosVisitVo> visitVos = new ArrayList<>();
            for (HosVisitDto visitDto : dto.getVisits()) {
                HosVisitVo visitVo = new HosVisitVo();
                BeanUtils.copyProperties(visitDto, visitVo);
                visitVo.setVisitId(visitDto.getId() == null ? null : Long.valueOf(visitDto.getId()));
                visitVo.setVisitNo(visitDto.getOrderNum());
                visitVo.setBaseInfo(visitDto.getBaseInfo());
                visitVo.setHealthInfo(visitDto.getHealthInfo());
                visitVos.add(visitVo);
            }
            vo.setHosVisits(visitVos);
        }
        return vo;
    }
}
