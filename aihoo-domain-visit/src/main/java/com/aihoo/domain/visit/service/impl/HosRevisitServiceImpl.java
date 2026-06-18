package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.entity.HosRevisit;
import com.aihoo.domain.visit.mapper.HosRevisitMapper;
import com.aihoo.domain.visit.service.HosRevisitService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 复诊 service 实现。
 */
@Service
public class HosRevisitServiceImpl extends ServiceImpl<HosRevisitMapper, HosRevisit> implements HosRevisitService {

    @Override
    public List<String> listSickIdsByDoctorUserId(String doctorId) {
        if (StringUtils.isEmpty(doctorId)) {
            return List.of();
        }
        List<HosRevisit> revisitList = baseMapper.selectList(new LambdaQueryWrapper<HosRevisit>()
                .select(HosRevisit::getHosSickId)
                .eq(HosRevisit::getDoctorUserId, doctorId));
        return revisitList.stream().map(HosRevisit::getHosSickId).distinct().toList();
    }
}