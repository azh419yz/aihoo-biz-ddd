package com.aihoo.domain.hospital.service.impl;

import com.aihoo.util.StringUtil;
import com.aihoo.domain.hospital.dto.TcmSyndromeDto;
import com.aihoo.domain.hospital.dto.TcmSyndromeListRequestDto;
import com.aihoo.domain.hospital.entity.TcmSyndrome;
import com.aihoo.domain.hospital.mapper.TcmSyndromeMapper;
import com.aihoo.domain.hospital.service.TcmSyndromeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TcmSyndromeServiceImpl extends ServiceImpl<TcmSyndromeMapper, TcmSyndrome> implements TcmSyndromeService {

    @Override
    public List<TcmSyndromeDto> getSyndromeList(TcmSyndromeListRequestDto req) {
        LambdaQueryWrapper<TcmSyndrome> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(TcmSyndrome::getStatus, 1);

        if (req != null && StringUtil.isNotBlank(req.getKeyword())) {
            queryWrapper.like(TcmSyndrome::getSyndromeName, req.getKeyword());
        }

        queryWrapper.orderByAsc(TcmSyndrome::getSortOrder);

        List<TcmSyndrome> list = this.list(queryWrapper);

        return list.stream().map(syndrome -> {
            TcmSyndromeDto dto = new TcmSyndromeDto();
            BeanUtils.copyProperties(syndrome, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
