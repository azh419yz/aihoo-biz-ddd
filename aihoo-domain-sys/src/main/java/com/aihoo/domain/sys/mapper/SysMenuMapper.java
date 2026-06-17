package com.aihoo.domain.sys.mapper;

import com.aihoo.domain.sys.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> listByUserId(Integer userId);

    List<SysMenu> listByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<SysMenu> listByRoleId(Integer roleId);
}
