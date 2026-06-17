package com.aihoo.domain.sys.mapper;

import com.aihoo.domain.sys.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface DictMapper extends BaseMapper<Dict> {
    List<Map> selectByType(String type);
    Dict selectByCode(@org.apache.ibatis.annotations.Param("type") String type, @org.apache.ibatis.annotations.Param("code") String code);
}
