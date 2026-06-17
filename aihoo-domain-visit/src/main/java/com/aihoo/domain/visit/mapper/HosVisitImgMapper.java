package com.aihoo.domain.visit.mapper;

import com.aihoo.domain.visit.entity.HosVisitImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface HosVisitImgMapper extends BaseMapper<HosVisitImg> {

    /**
     * 根据 hosVisitId 查询问诊图片列表（迁自 doctor-api 的同名 mapper 方法）。
     */
    List<HosVisitImg> selectByHosVisitId(String hosVisitId);
}
