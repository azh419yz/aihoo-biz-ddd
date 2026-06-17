package com.aihoo.domain.sys.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.dto.SysMenuDto;
import com.aihoo.domain.sys.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 菜单 service。
 */
public interface SysMenuService extends IService<SysMenu> {

    List<Map<String, Object>> userMenuButton(Integer userId);

    List<SysMenu> listByUserId(Integer userId);

    List<SysMenu> listByRoleIds(List<Integer> roleIds);

    List<SysMenu> listByRoleId(Integer roleId);

    boolean saveDto(SysMenu authorities);

    boolean updateByIdDto(SysMenu authorities);

    boolean updateDto(Map<String, Object> map);

    PageResult<SysMenu> getPage();
}
