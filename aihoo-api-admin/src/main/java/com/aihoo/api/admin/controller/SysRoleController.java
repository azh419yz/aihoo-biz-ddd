package com.aihoo.api.admin.controller;

import com.aihoo.common.BizResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.entity.SysMenu;
import com.aihoo.domain.sys.entity.SysRole;
import com.aihoo.domain.sys.service.SysMenuService;
import com.aihoo.domain.sys.service.SysRoleMenuService;
import com.aihoo.domain.sys.service.SysRoleService;
import com.aihoo.util.JSONUtil;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "SysRole", description = "角色管理")
@RestController
@RequestMapping("/api/v1/sysRole")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;
    private final SysMenuService sysMenuService;
    private final SysRoleMenuService roleMenuService;

    @PostMapping("/list")
    public BizResult<PageResult<SysRole>> list(@RequestBody Map<String, Object> map) {
        return BizResult.success(sysRoleService.getList(map));
    }

    @PostMapping("/add")
    public BizResult<Void> add(@RequestBody Map<String, Object> map) {
        if (map.get("roleName") == null || "".equals(map.get("roleName"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "角色名称必填");
        }
        return sysRoleService.saveDto(map)
                ? BizResult.success()
                : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "添加角色出错");
    }

    @PostMapping("/update")
    public BizResult<Void> update(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null || "".equals(map.get("id"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "角色id必填");
        }
        return sysRoleService.updateDto(map)
                ? BizResult.success()
                : BizResult.fail(com.aihoo.common.BizResultCode.NOT_FOUND, "不存在的id");
    }

    @PostMapping("/delete")
    public BizResult<Void> delete(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null || "".equals(map.get("id"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "角色id必填");
        }
        return sysRoleService.delDto(map)
                ? BizResult.success()
                : BizResult.fail(com.aihoo.common.BizResultCode.NOT_FOUND, "不存在的id" + map.get("id"));
    }

    @GetMapping("/authTree")
    public BizResult<List<Map<String, Object>>> authTree(Integer roleId) {
        List<SysMenu> roleAuths = sysMenuService.listByRoleId(roleId);
        List<SysMenu> allAuths = sysMenuService.list();
        List<Map<String, Object>> authTrees = new ArrayList<>();
        for (SysMenu one : allAuths) {
            Map<String, Object> authTree = new HashMap<>();
            authTree.put("id", one.getId());
            authTree.put("name", one.getMenuName() + " " + StringUtil.getStr(one.getPermission()));
            authTree.put("pId", one.getParentId());
            authTree.put("open", true);
            authTree.put("checked", false);
            for (SysMenu temp : roleAuths) {
                if (temp.getId().equals(one.getId())) {
                    authTree.put("checked", true);
                    break;
                }
            }
            authTrees.add(authTree);
        }
        return BizResult.success(authTrees);
    }

    @PostMapping("/updateRoleAuth")
    public BizResult<Void> updateRoleAuth(@RequestBody Map<String, Object> map) {
        if (map.get("roleId") == null || "".equals(map.get("roleId"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "角色id必填");
        }
        if (map.get("authIds") == null || "".equals(map.get("authIds"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "权限id必填");
        }
        boolean ok = roleMenuService.updateRoleAuth(Integer.valueOf(map.get("roleId").toString()),
                JSONUtil.parseArray(map.get("authIds").toString(), Integer.class));
        return ok ? BizResult.success("修改成功") : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "修改失败");
    }

    @PostMapping("/menuList")
    public BizResult<List<Map<String, Object>>> menuList(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null || "".equals(map.get("id"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请携带角色id");
        }
        List<SysMenu> roleAuths = sysMenuService.listByRoleId(Integer.valueOf(map.get("id").toString()));
        List<String> ex = new ArrayList<>();
        if (!roleAuths.isEmpty()) {
            ex = roleAuths.stream().map(SysMenu::getId).collect(Collectors.toList());
        }
        List<SysMenu> authorities = sysMenuService.list(
                new QueryWrapper<SysMenu>().eq("deleted", 0).orderByDesc("order_number"));
        List<String> loseIds = new ArrayList<>();
        List<String> ids = authorities.stream().filter(s -> "-1".equals(s.getParentId())).map(SysMenu::getId).collect(Collectors.toList());
        List<String> collect = authorities.stream().map(SysMenu::getParentId).collect(Collectors.toList());
        ids.forEach(id -> {
            if (!collect.contains(id)) {
                loseIds.add(id);
            }
        });
        List<Map<String, Object>> menuTree = getMenuTree(authorities, "-1", ex, loseIds);
        return BizResult.success(menuTree);
    }

    private List<Map<String, Object>> getMenuTree(List<SysMenu> authorities, String parentId, List<String> ex, List<String> loseIds) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < authorities.size(); i++) {
            SysMenu temp = authorities.get(i);
            if (parentId.equals(temp.getParentId())) {
                Map<String, Object> map = new HashMap<>();
                map.put("is_menu", temp.getIsMenu());
                map.put("id", temp.getId());
                map.put("menuName", temp.getMenuName());
                map.put("menuIcon", temp.getMenuIcon());
                map.put("menuUrl", temp.getMenuUrl());

                if (ex.contains(temp.getId()) && !temp.getParentId().equals("-1")) {
                    map.put("check", "1");
                } else {
                    if (ex.contains(temp.getId()) && loseIds.contains(temp.getId()) && temp.getParentId().equals("-1")) {
                        map.put("check", "1");
                    } else {
                        map.put("check", "0");
                    }
                }
                map.put("subMenus", getMenuTree(authorities, authorities.get(i).getId(), ex, loseIds));
                list.add(map);
            }
        }
        return list;
    }
}
