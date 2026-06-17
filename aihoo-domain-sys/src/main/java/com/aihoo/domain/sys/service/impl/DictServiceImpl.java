package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.dto.DictTypeItemDto;
import com.aihoo.domain.sys.entity.Dict;
import com.aihoo.domain.sys.mapper.DictMapper;
import com.aihoo.domain.sys.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final DictMapper dictMapper;

    public DictServiceImpl(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public List<DictTypeItemDto> getDoctorType(String type) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.orderByAsc("`index`");
        List<Dict> dicts = dictMapper.selectList(wrapper);
        List<DictTypeItemDto> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(dicts)) {
            return result;
        }
        for (Dict dict : dicts) {
            DictTypeItemDto dto = new DictTypeItemDto();
            dto.setTypeName(dict.getTypeName());
            dto.setCode(dict.getCode());
            dto.setName(dict.getName());
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<Map<String, String>> listByType(String type) {
        List<Map> maps = dictMapper.selectByType(type);
        List<Map<String, String>> result = new ArrayList<>();
        if (maps == null) {
            return result;
        }
        for (Map m : maps) {
            Map<String, String> item = new HashMap<>();
            for (Object key : m.keySet()) {
                item.put(String.valueOf(key), m.get(key) == null ? null : String.valueOf(m.get(key)));
            }
            result.add(item);
        }
        return result;
    }
}
