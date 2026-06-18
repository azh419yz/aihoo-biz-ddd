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

/**
 * 医生用户表 Mapper（迁移自 aihoo-biz-service/aihoo-doctor-api 的 DoctorUserMapper）。
 *
 * @author mcp
 * @since 2020-09-15
 */
@Mapper
public interface DoctorUserMapper extends BaseMapper<DoctorUser> {

    /**
     * 根据 mobile 查询医生（兼容旧 doctor-api DoctorUserMapper.selectByMobile 行为）。
     * 使用 mybatis-plus 自带 ${ew.customSqlSegment} 注入 Wrapper，复用 IService 的 LambdaQueryWrapper。
     */
    DoctorUser selectByMobile(@Param(Constants.WRAPPER) Wrapper<DoctorUser> wrapper);

    IPage<DoctorUser> selectDoctorUserPage(Page<DoctorUser> setPage, @Param("map") Map<String, Object> map);

}
