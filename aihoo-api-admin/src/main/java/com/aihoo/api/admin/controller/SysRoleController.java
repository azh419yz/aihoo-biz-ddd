package com.aihoo.api.admin.controller;


import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.model.entity.SysMenu;
import com.aihoo.domain.sys.model.entity.SysRole;
import com.aihoo.domain.sys.service.SysMenuService;
import com.aihoo.domain.sys.service.SysRoleMenuService;
import com.aihoo.domain.sys.service.SysRoleService;
import com.aihoo.domain.sys.service.SysUserRoleService;
import com.aihoo.util.JSONUtil;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
@RestController
@RequestMapping("api/v1/sysRole")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysRoleMenuService roleMenuService;


    /**
     * 查询所有角色
     **/
    @PostMapping("/list")
    public PageResult<SysRole> list(@RequestBody Map<String, Object> map) {
        try {
            return (PageResult) (Object) sysRoleService.getList(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("用户分页查询出错");
        }
    }

    /**
     * 添加角色
     **/
    @RequestMapping("/add")
    public JsonResult add(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("roleName") || "".equals(map.get("roleName"))) {
                return error("角色名称必填");
            }
//            SysRole sysRole = new SysRole();
//            sysRole.setRoleName(map.get("roleName").toString());
//            if (null != map.get("comments") && !"".equals(map.get("comments"))) {
//                sysRole.setComments(map.get("comments").toString());
//            }
//            sysRole.setCreateUser(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());

            boolean boo = sysRoleService.saveDto(map);
            return boo ? JsonResult.ok() : error("添加角色出错");
        } catch (Exception e) {
            e.printStackTrace();
            return error("添加角色出错");
        }
    }

    /**
     * 修改角色
     **/
    @RequestMapping("/update")
    public JsonResult update(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("id") || "".equals(map.get("id"))) {
                return error("角色id必填");
            }
//            SysRole sysRole = new SysRole();
//            if (null != map.get("roleName") && !"".equals(map.get("roleName"))) {
//                sysRole.setRoleName(map.get("roleName").toString());
//            }
//            if (null != map.get("comments") && !"".equals(map.get("comments"))) {
//                sysRole.setComments(map.get("comments").toString());
//            }
//            boolean boo = sysRoleService.update(sysRole,new QueryWrapper<SysRole>().eq("id",map.get("id")));
            boolean boo = sysRoleService.updateDto(map);
            return boo ? JsonResult.ok() : error("不存在的id");
        } catch (Exception e) {
            e.printStackTrace();
            return error("修改角色出错");
        }
    }

    /**
     * 删除角色
     **/
    @RequestMapping("/delete")
    public JsonResult delete(@RequestBody Map<String, Object> map) {

        try {
            if (null == map.get("id") || "".equals(map.get("id"))) {
                return error("角色id必填");
            }
//            SysRole sysRole = new SysRole();
//            sysRole.setDeleted("1");
//            boolean boo = sysRoleService.update(sysRole,new QueryWrapper<SysRole>().eq("id",map.get("id")));
            boolean boo = sysRoleService.delDto(map);
            return boo ? JsonResult.ok() : error("不存在的id" + map.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return error("角色删除出错");
        }

    }

    /**
     * 角色权限树
     */
    @GetMapping("/authTree")
    public List<Map<String, Object>> authTree(Integer roleId) {
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
        return authTrees;
    }

    /**
     * 修改角色权限和新增
     */
    @PostMapping("/updateRoleAuth")
    public JsonResult updateRoleAuth(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("roleId") || "".equals(map.get("roleId"))) {
                return error("角色id必填");
            }
            if (null == map.get("authIds") || "".equals(map.get("authIds"))) {
                return error("权限id必填");
            }
            if (roleMenuService.updateRoleAuth(Integer.valueOf(map.get("roleId").toString()), JSONUtil.parseArray(map.get("authIds").toString(), Integer.class))) {
                return ok("修改成功");
            }
            return error("修改失败");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return error("修改角色权限出错");
        }
    }


    /**
     * 角色的权限树列表
     *
     * @return {}
     */
    @PostMapping("/menuList")
    public JsonResult menuList(@RequestBody Map<String, Object> map) {
        if (null == map.get("id") || "".equals(map.get("id"))) {
            return error("请携带角色id");
        }
        //该用户已经存在的权限
        List<SysMenu> roleAuths = sysMenuService.listByRoleId(Integer.valueOf(map.get("id").toString()));
        List<String> ex = new ArrayList<>();
        if (!roleAuths.isEmpty()) {
            ex = roleAuths.stream().map(SysMenu::getId).collect(Collectors.toList());
        }
        //所有的权限
        List<SysMenu> authorities = sysMenuService.list
                (new QueryWrapper<SysMenu>().eq("deleted", 0)
                        .orderByDesc("order_number"));
        //没有子集权限的父级权限
        List<String> loseIds = new ArrayList<>();
        List<String> ids = authorities.stream().filter(s -> "-1".equals(s.getParentId())).map(SysMenu::getId).collect(Collectors.toList());
        List<String> collect = authorities.stream().map(SysMenu::getParentId).collect(Collectors.toList());
        ids.forEach(id -> {
            if (!collect.contains(id)) {
                loseIds.add(id);
            }
        });
        List<Map<String, Object>> menuTree = getMenuTree(authorities, "-1", ex, loseIds);

        return JsonResult.ok().put("data", menuTree);
    }

    // 递归转化树形菜单
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
                /*if ("52".equals(temp.getId()) || "50".equals(temp.getId())){
                    //当权限id为 诊疗服务管理 或者 当前会话 为必定选中 ，每个系统用户都有此权限
                    map.put("required","1");
                }else {
                    //默认不必选
                    map.put("required","0");
                }*/
                // 父级菜单不显示，因为前段组件父级勾中下面默认全部选中,当父子菜单没有子集的时候，应设定选中

                if (ex.contains(temp.getId()) && !temp.getParentId().equals("-1")) {
                    //包含id是说明选中   1 表示true 选中  0 反之
                    map.put("check", "1");
                } else {
                    if (ex.contains(temp.getId()) && loseIds.contains(temp.getId()) && temp.getParentId().equals("-1")) {
                        // 有该权限，是父级，且没有子集权限的菜单勾中
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
