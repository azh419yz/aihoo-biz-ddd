package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {

    List<String> userRole();
}
