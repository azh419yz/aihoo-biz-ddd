package com.aihoo.domain.doctor.mapper;

import com.aihoo.domain.doctor.entity.DoctorUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 医生用户表 Mapper（迁移自 aihoo-biz-service/aihoo-doctor-api 的 DoctorUserMapper）。
 *
 * @author mcp
 * @since 2020-09-15
 */
@Mapper
public interface DoctorUserMapper extends BaseMapper<DoctorUser> {

    List<DoctorUser> findTeamDoctorByMdtOrderNum(@Param("isMain") String isMain,
                                                  @Param("mdtOrderNum") String mdtOrderNum);
}