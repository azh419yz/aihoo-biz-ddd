package com.aihoo.domain.doctor.mapper;

import com.aihoo.domain.doctor.entity.DoctorUser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface DoctorUserMapper extends BaseMapper<DoctorUser> {

    
    DoctorUser selectByMobile(@Param(Constants.WRAPPER) Wrapper<DoctorUser> wrapper);

    IPage<DoctorUser> selectDoctorUserPage(Page<DoctorUser> setPage, @Param("map") Map<String, Object> map);

}
