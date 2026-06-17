package com.aihoo.domain.sys.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.entity.SysMenu;
import com.aihoo.domain.sys.mapper.SysMenuMapper;
import com.aihoo.domain.sys.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> listByUserId(Integer userId) {
        return baseMapper.listByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> userMenuButton(Integer userId) {
        List<SysMenu> sysMenus = baseMapper.listByUserId(userId);
        return getMenuTree(sysMenus, "-1");
    }

    private List<Map<String, Object>> getMenuTree(List<SysMenu> authorities, String parentId) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < authorities.size(); i++) {
            SysMenu temp = authorities.get(i);
            if (parentId.equals(temp.getParentId())) {
                Map<String, Object> map = new HashMap<>();
                map.put("is_menu", temp.getIsMenu());
                map.put("menuName", temp.getMenuName());
                map.put("menuIcon", temp.getMenuIcon());
                map.put("menuUrl", temp.getMenuUrl());
                map.put("subMenus", getMenuTree(authorities, authorities.get(i).getId()));
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public List<SysMenu> listByRoleId(Integer roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        return baseMapper.listByRoleId(roleId);
    }

    @Override
    public List<SysMenu> listByRoleIds(List<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return new ArrayList<>();
        }
        return baseMapper.listByRoleIds(roleIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveDto(SysMenu authorities) {
        return this.save(authorities);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByIdDto(SysMenu authorities) {
        return this.updateById(authorities);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDto(Map<String, Object> map) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("id", map.get("id"));
        SysMenu sysMenu = new SysMenu();
        sysMenu.setDeleted("1");
        return this.update(sysMenu, wrapper);
    }

    @Override
    public PageResult<SysMenu> getPage() {
        long page = 1;
        long limit = 10;
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("order_number");
        wrapper.eq("deleted", 0);
        IPage<SysMenu> iPage = this.page(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }
}
