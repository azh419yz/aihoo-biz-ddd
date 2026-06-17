package com.aihoo.domain.sys.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.entity.SysRole;
import com.aihoo.domain.sys.mapper.SysRoleMapper;
import com.aihoo.domain.sys.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public PageResult<SysRole> getList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        if (null != map.get("roleName") && !"".equals(map.get("roleName"))) {
            wrapper.like("role_name", map.get("roleName").toString());
        }
        wrapper.orderByDesc("created_date");
        IPage<SysRole> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveDto(Map<String, Object> map) {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName(map.get("roleName").toString());
        if (null != map.get("comments") && !"".equals(map.get("comments"))) {
            sysRole.setComments(map.get("comments").toString());
        }
        sysRole.setCreateUser(Objects.requireNonNull(com.aihoo.util.SecurityUtils.getLoginUserId()));
        return this.save(sysRole);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDto(Map<String, Object> map) {
        SysRole sysRole = new SysRole();
        if (null != map.get("roleName") && !"".equals(map.get("roleName"))) {
            sysRole.setRoleName(map.get("roleName").toString());
        }
        if (null != map.get("comments") && !"".equals(map.get("comments"))) {
            sysRole.setComments(map.get("comments").toString());
        }
        return this.update(sysRole, new QueryWrapper<SysRole>().eq("id", map.get("id")));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delDto(Map<String, Object> map) {
        SysRole sysRole = new SysRole();
        sysRole.setDeleted("1");
        return this.update(sysRole, new QueryWrapper<SysRole>().eq("id", map.get("id")));
    }
}
