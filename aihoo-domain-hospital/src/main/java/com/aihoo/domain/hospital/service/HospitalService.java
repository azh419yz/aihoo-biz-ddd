package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.entity.Hospital;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface HospitalService extends IService<Hospital> {

    
    PageResult<Hospital> list(Map<String, Object> map);

    
    com.alibaba.fastjson2.JSONObject controlHospital(String id);

    
    com.alibaba.fastjson2.JSONArray departmentRelation();

    
    com.alibaba.fastjson2.JSONObject hospitalUpdate(Map<String, Object> map, HttpServletRequest request);

    
    com.alibaba.fastjson2.JSONObject hospitalInsert(Map<String, Object> map, HttpServletRequest request);

    
    com.alibaba.fastjson2.JSONArray provincesRelation();

    
    Boolean enableDisable(Map<String, Object> map, HttpServletRequest request);

    
    Boolean updateDel(Map<String, Object> map, HttpServletRequest request);
}
