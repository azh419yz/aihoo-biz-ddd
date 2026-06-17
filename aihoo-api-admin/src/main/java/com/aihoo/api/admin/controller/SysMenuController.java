package com.aihoo.api.admin.controller;

import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.model.entity.SysMenu;
import com.aihoo.domain.sys.service.SysMenuService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
@RestController
@RequestMapping("/api/v1/sys/menu")
public class SysMenuController extends BaseController {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 查询所有权限
     **/
    @RequestMapping("/list")
    public PageResult<SysMenu> list() {
        try {
            return sysMenuService.getPage();
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("列表查询出错");
        }
    }

    /**
     * 添加权限
     */
    @RequestMapping("/add")
    public JsonResult add(@RequestBody SysMenu authorities) {
        try {
            if (null == authorities.getMenuName() || authorities.getMenuName().equals("")) {
                return error("权限名称必填");
            }
            if (null == authorities.getParentId() || authorities.getParentId().equals("")) {
                return error("父id必传");
            }
            if (null == authorities.getIsMenu() || authorities.getIsMenu().equals("")) {
                return error("权限类型必填");
            }
            if (sysMenuService.saveDto(authorities)) {
                return ok("添加成功");
            }
            return error("添加失败");
        } catch (Exception e) {
            e.printStackTrace();
            return error("添加失败");
        }
    }

    /**
     * 修改权限
     */
    @RequestMapping("/update")
    public JsonResult update(@RequestBody SysMenu authorities) {
        try {
            if (null == authorities.getId()) {
                return error("id必传");
            }
            if (null != authorities.getDeleted()) {
                authorities.setDeleted("0");
            }
            if (sysMenuService.updateByIdDto(authorities)) {
                return JsonResult.ok("修改成功");
            }
            return JsonResult.error("修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            return error("修改权限出错");
        }
    }

    /**
     * 删除权限
     */
    @RequestMapping("/delete")
    public JsonResult delete(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("id") || "".equals(map.get("id"))) {
                return error("必须携带id");
            }

            if (sysMenuService.updateDto(map)) {
                return ok("删除成功");
            }
            return error("删除失败");

        } catch (Exception e) {
            e.printStackTrace();
            return error("删除权限出错");
        }
    }
}
