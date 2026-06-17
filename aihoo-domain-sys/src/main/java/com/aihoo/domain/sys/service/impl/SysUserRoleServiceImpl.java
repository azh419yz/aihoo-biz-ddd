package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.entity.SysUserRole;
import com.aihoo.domain.sys.mapper.SysUserRoleMapper;
import com.aihoo.domain.sys.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public List<String> userRole() {
        return List.of();
    }
}
