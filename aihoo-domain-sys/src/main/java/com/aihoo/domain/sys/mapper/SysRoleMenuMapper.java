package com.aihoo.domain.sys.mapper;

import com.aihoo.domain.sys.entity.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    int insertRoleAuths(@Param("roleId") Integer roleId, @Param("authIds") List<Integer> authIds);
}
