package com.aihoo.domain.visit.mapper;

import com.aihoo.domain.visit.entity.HosVisitImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface HosVisitImgMapper extends BaseMapper<HosVisitImg> {

    
    List<HosVisitImg> selectByHosVisitId(String hosVisitId);
}
