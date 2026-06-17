package com.aihoo.domain.visit.mapper;

import com.aihoo.domain.visit.dto.HosVisitVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HosVisitVoMapper {

    List<HosVisitVo> orderList(Map<String, Object> map);

    List<HosVisitVo> patientList(Map<String, Object> map);

    List<HosVisitVo> orderDetails(Map<String, Object> map);
}