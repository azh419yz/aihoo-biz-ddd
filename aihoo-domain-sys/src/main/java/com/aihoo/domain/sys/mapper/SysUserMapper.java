package com.aihoo.domain.sys.mapper;

import com.aihoo.domain.sys.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser selectByUsername(String username);
}
