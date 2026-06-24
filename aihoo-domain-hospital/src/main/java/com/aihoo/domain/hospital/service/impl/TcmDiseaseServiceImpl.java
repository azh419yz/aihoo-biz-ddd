package com.aihoo.domain.hospital.service.impl;

import com.aihoo.util.StringUtil;
import com.aihoo.domain.hospital.dto.TcmDiseaseDto;
import com.aihoo.domain.hospital.dto.TcmDiseaseListRequestDto;
import com.aihoo.domain.hospital.entity.TcmDisease;
import com.aihoo.domain.hospital.mapper.TcmDiseaseMapper;
import com.aihoo.domain.hospital.service.TcmDiseaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TcmDiseaseServiceImpl extends ServiceImpl<TcmDiseaseMapper, TcmDisease> implements TcmDiseaseService {

    @Override
    public List<TcmDiseaseDto> getDiseaseList(TcmDiseaseListRequestDto req) {
        LambdaQueryWrapper<TcmDisease> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(TcmDisease::getStatus, 1);

        if (req != null && StringUtil.isNotBlank(req.getKeyword())) {
            String keyword = req.getKeyword();
            queryWrapper.and(wrapper ->
                    wrapper.like(TcmDisease::getDiseaseName, keyword)
                            .or()
                            .like(TcmDisease::getDiseasePinyinInitial, keyword.toUpperCase())
            );
        }

        queryWrapper.orderByAsc(TcmDisease::getSortOrder);

        List<TcmDisease> list = this.list(queryWrapper);

        return list.stream().map(disease -> {
            TcmDiseaseDto dto = new TcmDiseaseDto();
            BeanUtils.copyProperties(disease, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
