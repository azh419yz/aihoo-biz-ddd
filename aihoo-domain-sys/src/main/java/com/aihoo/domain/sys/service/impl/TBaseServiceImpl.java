package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.dto.TBaseDto;
import com.aihoo.domain.sys.entity.TBase;
import com.aihoo.domain.sys.mapper.TBaseMapper;
import com.aihoo.domain.sys.service.TBaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TBaseServiceImpl extends ServiceImpl<TBaseMapper, TBase> implements TBaseService {

    private static final String COMMON = "USEFUL_EXPRESSIONS";

    private final TBaseMapper tBaseMapper;

    public TBaseServiceImpl(TBaseMapper tBaseMapper) {
        this.tBaseMapper = tBaseMapper;
    }

    @Override
    public List<TBaseDto> pageList() {
        QueryWrapper<TBase> wrapper = new QueryWrapper<>();
        wrapper.eq("`key`", COMMON);
        List<TBase> list = tBaseMapper.selectList(wrapper);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(v -> {
            TBaseDto dto = new TBaseDto();
            BeanUtils.copyProperties(v, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean addCommonWords(Map<String, Object> map) {
        TBase tBase = new TBase();
        tBase.setKey(COMMON);
        tBase.setCreateUserId(Objects.requireNonNull(com.aihoo.util.SecurityUtils.getLoginUserId()));
        tBase.setTitle("常用语");
        tBase.setContent(String.valueOf(map.get("content")));
        tBase.setIndex(String.valueOf(map.get("index")));
        return tBaseMapper.insert(tBase) > 0;
    }

    @Override
    public boolean updateCommonWords(Map<String, Object> map) {
        TBase tBase = tBaseMapper.selectById(String.valueOf(map.get("id")));
        if (tBase == null) {
            return false;
        }
        if (com.aihoo.util.StringHandler.isNotBlank(String.valueOf(map.get("content")))) {
            tBase.setContent(String.valueOf(map.get("content")));
        }
        if (com.aihoo.util.StringHandler.isNotBlank(String.valueOf(map.get("index")))) {
            tBase.setIndex(String.valueOf(map.get("index")));
        }
        return tBaseMapper.updateById(tBase) > 0;
    }
}
