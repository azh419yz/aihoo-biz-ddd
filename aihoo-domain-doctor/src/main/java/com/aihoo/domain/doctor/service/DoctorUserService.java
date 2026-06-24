package com.aihoo.domain.doctor.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.dto.DoctorEnableDisableRequestDto;
import com.aihoo.domain.doctor.dto.DoctorUserAddRequestDto;
import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.dto.DoctorUserDto;
import com.aihoo.domain.doctor.dto.DoctorUserUpdateRequestDto;
import com.aihoo.domain.doctor.dto.DoctorVisitSetDto;
import com.aihoo.domain.doctor.dto.DoctorWelcomeMessageDto;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.entity.DoctorWelcomeMessageSet;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface DoctorUserService extends IService<DoctorUser> {

    PageResult<DoctorUser> list(Map<String, Object> map);

    
    List<DoctorUser> doctorQuery(String name);

    
    DoctorUserDetailsDto doctorDetails(String id);

    JSONArray hospitalDepartmentAll(String hospitalId);

    
    String getNowWelcomeMessage(Long doctorUserId);

    
    DoctorUserDto loginUser(String mobile, HttpServletRequest request);

    
    DoctorUser selectMobile(String mobile);

    
    boolean sendCode(String mobile);

    
    DoctorUserDto phoneLogin(String mobile, String code, HttpServletRequest request);

    
    DoctorVisitSet getVisitSet();

    
    DoctorVisitSet setVisit(DoctorVisitSetDto request);

    
    DoctorWelcomeMessageSet getWelcomeMessage();

    
    DoctorWelcomeMessageSet setWelcomeMessage(DoctorWelcomeMessageDto request);

    
    DoctorUserDto detail(String id);

    
    void doctorUserAdd(DoctorUserAddRequestDto request, HttpServletRequest httpRequest) throws Exception;

    
    void doctorUpdate(DoctorUserUpdateRequestDto request, HttpServletRequest httpRequest) throws Exception;

    
    boolean enableDisable(DoctorEnableDisableRequestDto request);

}
