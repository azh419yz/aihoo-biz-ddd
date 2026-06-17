package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.entity.SysMenu;
import com.aihoo.domain.sys.entity.SysRoleMenu;
import com.aihoo.domain.sys.mapper.SysMenuMapper;
import com.aihoo.domain.sys.mapper.SysRoleMenuMapper;
import com.aihoo.domain.sys.service.SysRoleMenuService;
import com.aihoo.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleAuth(Integer roleId, List<Integer> authIds) {
        if (CollectionUtils.isEmpty(authIds)) {
            baseMapper.delete(new UpdateWrapper<SysRoleMenu>().eq("role_id", roleId));
            return true;
        }
        List<SysMenu> sysMenus = this.sysMenuMapper.selectList(new QueryWrapper<SysMenu>().in("id", authIds));
        List<String> parentIds = sysMenus.stream().map(SysMenu::getParentId).filter(s -> !"-1".equals(s)).distinct().collect(Collectors.toList());
        parentIds.forEach(s -> {
            if (!authIds.contains(Integer.valueOf(s))) {
                authIds.add(Integer.valueOf(s));
            }
        });

        baseMapper.delete(new UpdateWrapper<SysRoleMenu>().eq("role_id", roleId));
        if (authIds != null && authIds.size() > 0) {
            if (baseMapper.insertRoleAuths(roleId, authIds) < authIds.size()) {
                throw new BizException("操作失败");
            }
        }
        return true;
    }
}
