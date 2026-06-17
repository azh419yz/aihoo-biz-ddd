package com.aihoo.domain.tcm.service.impl;

import com.aihoo.util.StringUtil;
import com.aihoo.domain.tcm.dto.TcmDiseaseDto;
import com.aihoo.domain.tcm.dto.TcmDiseaseListRequestDto;
import com.aihoo.domain.tcm.entity.TcmDisease;
import com.aihoo.domain.tcm.mapper.TcmDiseaseMapper;
import com.aihoo.domain.tcm.service.TcmDiseaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 中医辨病服务实现类（迁自 doctor-api 的 TcmDiseaseServiceImpl）。
 */
@Service
public class TcmDiseaseServiceImpl extends ServiceImpl<TcmDiseaseMapper, TcmDisease> implements TcmDiseaseService {

    @Override
    public List<TcmDiseaseDto> getDiseaseList(TcmDiseaseListRequestDto req) {
        LambdaQueryWrapper<TcmDisease> queryWrapper = new LambdaQueryWrapper<>();

        // 状态（0-禁用 1-启用）
        queryWrapper.eq(TcmDisease::getStatus, 1);

        // 模糊搜索：疾病名称或拼音首字母
        if (req != null && StringUtil.isNotBlank(req.getKeyword())) {
            String keyword = req.getKeyword();
            queryWrapper.and(wrapper ->
                    wrapper.like(TcmDisease::getDiseaseName, keyword)
                            .or()
                            .like(TcmDisease::getDiseasePinyinInitial, keyword.toUpperCase())
            );
        }

        // 按排序序号排序
        queryWrapper.orderByAsc(TcmDisease::getSortOrder);

        List<TcmDisease> list = this.list(queryWrapper);

        return list.stream().map(disease -> {
            TcmDiseaseDto dto = new TcmDiseaseDto();
            BeanUtils.copyProperties(disease, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
