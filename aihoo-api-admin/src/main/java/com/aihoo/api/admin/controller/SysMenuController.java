package com.aihoo.api.admin.controller;

import com.aihoo.common.BizResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.entity.SysMenu;
import com.aihoo.domain.sys.service.SysMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "SysMenu", description = "菜单管理")
@RestController
@RequestMapping("/api/v1/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @RequestMapping("/list")
    public BizResult<PageResult<SysMenu>> list() {
        return BizResult.success(sysMenuService.getPage());
    }

    @RequestMapping("/add")
    public BizResult<Void> add(@RequestBody SysMenu authorities) {
        if (authorities.getMenuName() == null || authorities.getMenuName().isEmpty()) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "权限名称必填");
        }
        if (authorities.getParentId() == null || authorities.getParentId().isEmpty()) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "父id必传");
        }
        if (authorities.getIsMenu() == null || authorities.getIsMenu().isEmpty()) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "权限类型必填");
        }
        return sysMenuService.saveDto(authorities)
                ? BizResult.success("添加成功")
                : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "添加失败");
    }

    @RequestMapping("/update")
    public BizResult<Void> update(@RequestBody SysMenu authorities) {
        if (authorities.getId() == null) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "id必传");
        }
        if (authorities.getDeleted() != null) {
            authorities.setDeleted("0");
        }
        return sysMenuService.updateByIdDto(authorities)
                ? BizResult.success("修改成功")
                : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "修改失败");
    }

    @RequestMapping("/delete")
    public BizResult<Void> delete(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null || "".equals(map.get("id"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "必须携带id");
        }
        return sysMenuService.updateDto(map)
                ? BizResult.success("删除成功")
                : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "删除失败");
    }
}
