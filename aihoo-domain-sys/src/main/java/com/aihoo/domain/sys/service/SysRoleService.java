package com.aihoo.domain.sys.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 角色 service。
 */
public interface SysRoleService extends IService<SysRole> {

    PageResult<SysRole> getList(Map<String, Object> map);

    boolean saveDto(Map<String, Object> map);

    boolean updateDto(Map<String, Object> map);

    boolean delDto(Map<String, Object> map);
}
