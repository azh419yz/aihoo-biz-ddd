package com.aihoo.domain.tcm.service.impl;

import com.aihoo.util.StringUtil;
import com.aihoo.domain.tcm.dto.TcmSyndromeDto;
import com.aihoo.domain.tcm.dto.TcmSyndromeListRequestDto;
import com.aihoo.domain.tcm.entity.TcmSyndrome;
import com.aihoo.domain.tcm.mapper.TcmSyndromeMapper;
import com.aihoo.domain.tcm.service.TcmSyndromeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 中医辨证服务实现类（迁自 doctor-api 的 TcmSyndromeServiceImpl）。
 */
@Service
public class TcmSyndromeServiceImpl extends ServiceImpl<TcmSyndromeMapper, TcmSyndrome> implements TcmSyndromeService {

    @Override
    public List<TcmSyndromeDto> getSyndromeList(TcmSyndromeListRequestDto req) {
        LambdaQueryWrapper<TcmSyndrome> queryWrapper = new LambdaQueryWrapper<>();

        // 状态（0-禁用 1-启用）
        queryWrapper.eq(TcmSyndrome::getStatus, 1);

        // 证候名称模糊匹配
        if (req != null && StringUtil.isNotBlank(req.getKeyword())) {
            queryWrapper.like(TcmSyndrome::getSyndromeName, req.getKeyword());
        }

        // 按排序序号排序
        queryWrapper.orderByAsc(TcmSyndrome::getSortOrder);

        List<TcmSyndrome> list = this.list(queryWrapper);

        return list.stream().map(syndrome -> {
            TcmSyndromeDto dto = new TcmSyndromeDto();
            BeanUtils.copyProperties(syndrome, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
