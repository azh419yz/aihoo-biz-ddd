package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.controller.vo.SysUserVo;
import com.aihoo.common.*;
import com.aihoo.constant.PasswordRegex;
import com.aihoo.domain.sys.model.dto.SaveUpdateUserRequest;
import com.aihoo.domain.sys.model.dto.SearchUserRequest;
import com.aihoo.domain.sys.model.entity.SysRole;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.service.SysRoleService;
import com.aihoo.domain.sys.service.SysUserService;
import com.aihoo.enums.UserRoleEnum;
import com.aihoo.util.SecurityUtils;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aihoo.util.SecurityUtils.getLoginUser;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
@Tag(name = "SysUserController", description = "运营端-用户相关接口")
@RestController
@RequestMapping("/api/v1/sys/user")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private static final String DEFAULT_PSW = "abc!1234";  // 用户默认密码
    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;


    @Operation(summary = "查询用户列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PageResult.class},
                            description = "查询用户列表"
                    )
            )
    )
    @GetMapping("/list")
    public BizResult<PageResult<SysUserVo>> list(@ParameterObject PageParam<SysUser> pageParam,
                                                 @ParameterObject SearchUserRequest request) {
        return BizResult.success((PageResult) (Object) sysUserService.listUser(pageParam, request));
    }

    @Operation(summary = "添加用户")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "添加用户"
                    )
            )
    )
    @PutMapping("/add")
    public BizResult<Void> add(@Validated(SaveUpdateUserRequest.Save.class) @RequestBody SaveUpdateUserRequest request) {
        boolean result = sysUserService.addUser(request);
        return result ? BizResult.success("添加成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "添加失败");
    }

    @Operation(summary = "修改用户")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "修改用户"
                    )
            )
    )
    @PostMapping("/update")
    public BizResult<Void> update(@Validated(SaveUpdateUserRequest.Save.class) @RequestBody SaveUpdateUserRequest request) {
        boolean result = sysUserService.update(request);
        return result ? BizResult.success("更新成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "更新失败");
    }

    /**
     * 修改用户状态
     **/
    @RequestMapping("/updateState")
    public JsonResult updateState(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("userId") || "".equals(map.get("userId"))) {
                return error("用户id为必传参数");
            }
            if (null == map.get("status") || "".equals(map.get("status"))) {
                return error("状态必传");
            }
            if (!map.get("status").toString().equals("0") && !map.get("status").toString().equals("1")) {
                return error("状态值不正确");
            }
            boolean update = sysUserService.updateStatus(map);
            if (update) {
                return ok("修改成功");
            } else {
                return error("不存的id");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return error("修改用户出错");
        }
    }

    @Operation(summary = "删除用户")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "删除用户"
                    )
            )
    )
    @DeleteMapping("/delete")
    public BizResult<Void> delete(@RequestParam String id) {
        boolean result = sysUserService.isDelete(id);
        return result ? BizResult.success("删除成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "删除失败");
    }

    /**
     * 重置密码
     **/

    @RequestMapping("/restPsw")
    public JsonResult resetPsw(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("userId") || "".equals(map.get("userId"))) {
                return error("参数userId不能为空");
            }
            boolean b = sysUserService.resetPsw(map);
            if (b) {
                return ok("重置成功，初始密码为" + DEFAULT_PSW);
            } else {
                return error("重置失败");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return error("修改密码出错");
        }
    }

    /**
     * 修改自己密码
     **/
    @RequestMapping("/updatePsw")
    public JsonResult updatePsw(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("oldPsw") || "".equals(map.get("oldPsw"))) {
                return error("旧密码不能为空");
            }
            if (null == map.get("newPsw") || "".equals(map.get("newPsw"))) {
                return error("新密码不能为空");
            } else {
                if (!map.get("newPsw").toString().trim().matches(PasswordRegex.PASSWORD_MIXED_CASE)) {
                    return error("密码格式错误! 密码最短为8位，必须包含字母数字和特殊符号(~!@#$%^&*)三种组合。");
                }
            }
            if (getLoginUser() == null) {
                return error("未登录");
            }
            if (!((com.aihoo.domain.sys.model.entity.SysUser) getLoginUser()).getPassword().equals(SecurityUtils.encryptPassword(map.get("oldPsw").toString()))) {
                return error("原密码输入不正确");
            }
            boolean b = sysUserService.updatePsw(map);
            if (b) {
                //修改成功重新登录
                SecurityContextHolder.clearContext();
                return ok("修改成功,请重新登录");
            } else {
                return error("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("修改出错");
        }
    }

    /**
     * 查询已存在且没有删除的所有角色名称及对应的id
     *
     * @return {}
     */
    @PostMapping("/getRoleAll")
    public JsonResult getRoleAll() {
        try {
            List<String> roles = new ArrayList<>();
            roles.add(UserRoleEnum.HZZLYS.getCode());
            List<SysRole> sysRoles = this.sysRoleService.list(new QueryWrapper<SysRole>().eq("deleted", 0).notIn("id", roles).orderByDesc("created_date"));
            if (!sysRoles.isEmpty()) {
                JSONArray jsonArray = new JSONArray();
                sysRoles.forEach(sysRole -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("roleName", sysRole.getRoleName());
                    jsonObject.put("id", sysRole.getId());
                    jsonArray.add(jsonObject);
                });
                return ok("请求成功").put("data", jsonArray);
            } else {
                return ok("请求成功").put("data", Lists.newArrayList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("请求出错");
        }
    }
}
