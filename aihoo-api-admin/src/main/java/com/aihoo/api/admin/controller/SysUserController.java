package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.request.SaveUpdateUserRequest;
import com.aihoo.api.admin.request.SearchUserRequest;
import com.aihoo.api.admin.vo.SysUserVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.constant.PasswordRegex;
import com.aihoo.domain.sys.dto.SaveUpdateUserRequestDto;
import com.aihoo.domain.sys.dto.SearchUserRequestDto;
import com.aihoo.domain.sys.dto.SysUserDto;
import com.aihoo.domain.sys.entity.SysRole;
import com.aihoo.domain.sys.entity.SysUser;
import com.aihoo.domain.sys.service.SysRoleService;
import com.aihoo.domain.sys.service.SysUserService;
import com.aihoo.enums.UserRoleEnum;
import com.aihoo.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "SysUser", description = "运营端-用户相关接口")
@RestController
@RequestMapping("/api/v1/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private static final String DEFAULT_PSW = "abc!1234";

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;

    @Operation(summary = "查询用户列表")
    @GetMapping("/list")
    public BizResult<PageResult<SysUserVo>> list(@ParameterObject PageParam<SysUser> pageParam,
                                                 @ParameterObject SearchUserRequest request) {
        SearchUserRequestDto reqDto = new SearchUserRequestDto();
        BeanUtils.copyProperties(request, reqDto);
        PageResult<SysUserDto> page = sysUserService.listUser(pageParam, reqDto);
        return BizResult.success(new PageResult<>(
                page.getData().stream().map(this::toVo).collect(Collectors.toList()),
                page.getCount()));
    }

    @Operation(summary = "添加用户")
    @PutMapping("/add")
    public BizResult<Void> add(@Validated(SaveUpdateUserRequest.Save.class) @RequestBody SaveUpdateUserRequest request) {
        SaveUpdateUserRequestDto dto = new SaveUpdateUserRequestDto();
        BeanUtils.copyProperties(request, dto);
        return sysUserService.addUser(dto)
                ? BizResult.success("添加成功")
                : BizResult.fail(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "添加失败");
    }

    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public BizResult<Void> update(@Validated(SaveUpdateUserRequest.Save.class) @RequestBody SaveUpdateUserRequest request) {
        SaveUpdateUserRequestDto dto = new SaveUpdateUserRequestDto();
        BeanUtils.copyProperties(request, dto);
        return sysUserService.update(dto)
                ? BizResult.success("更新成功")
                : BizResult.fail(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "更新失败");
    }

    @PostMapping("/updateState")
    public BizResult<Void> updateState(@RequestBody Map<String, Object> map) {
        if (map.get("userId") == null || "".equals(map.get("userId"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "用户id为必传参数");
        }
        if (map.get("status") == null || "".equals(map.get("status"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "状态必传");
        }
        if (!"0".equals(map.get("status").toString()) && !"1".equals(map.get("status").toString())) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "状态值不正确");
        }
        boolean ok = sysUserService.updateStatus(map);
        return ok ? BizResult.success("修改成功") : BizResult.fail(com.aihoo.common.BizResultCode.NOT_FOUND, "不存在的id");
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete")
    public BizResult<Void> delete(@RequestParam String id) {
        return sysUserService.isDelete(id)
                ? BizResult.success("删除成功")
                : BizResult.fail(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "删除失败");
    }

    @PostMapping("/restPsw")
    public BizResult<Void> resetPsw(@RequestBody Map<String, Object> map) {
        if (map.get("userId") == null || "".equals(map.get("userId"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "参数userId不能为空");
        }
        boolean ok = sysUserService.resetPsw(map);
        return ok ? BizResult.success("重置成功，初始密码为" + DEFAULT_PSW) : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "重置失败");
    }

    @PostMapping("/updatePsw")
    public BizResult<Void> updatePsw(@RequestBody Map<String, Object> map) {
        if (map.get("oldPsw") == null || "".equals(map.get("oldPsw"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "旧密码不能为空");
        }
        if (map.get("newPsw") == null || "".equals(map.get("newPsw"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "新密码不能为空");
        }
        if (!map.get("newPsw").toString().trim().matches(PasswordRegex.PASSWORD_MIXED_CASE)) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "密码格式错误! 密码最短为8位，必须包含字母数字和特殊符号(~!@#$%^&*)三种组合。");
        }
        Object principal = SecurityUtils.getLoginUser();
        if (principal == null) {
            return BizResult.fail(com.aihoo.common.BizResultCode.UNAUTHORIZED, "未登录");
        }
        String currentPwd = SecurityUtils.extractPassword(principal);
        if (currentPwd == null || !currentPwd.equals(SecurityUtils.encryptPassword(map.get("oldPsw").toString()))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "原密码输入不正确");
        }
        boolean ok = sysUserService.updatePsw(map);
        if (ok) {
            SecurityContextHolder.clearContext();
            return BizResult.success("修改成功,请重新登录");
        }
        return BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "修改失败");
    }

    @PostMapping("/getRoleAll")
    public BizResult<List<Map<String, String>>> getRoleAll() {
        List<String> roles = new ArrayList<>();
        roles.add(UserRoleEnum.HZZLYS.getCode());
        List<SysRole> sysRoles = sysRoleService.list(new QueryWrapper<SysRole>().eq("deleted", 0).notIn("id", roles).orderByDesc("created_date"));
        if (sysRoles.isEmpty()) {
            return BizResult.success(Lists.newArrayList());
        }
        List<Map<String, String>> data = sysRoles.stream().map(r -> {
            Map<String, String> m = new java.util.HashMap<>();
            m.put("roleName", r.getRoleName());
            m.put("id", r.getId());
            return m;
        }).collect(Collectors.toList());
        return BizResult.success(data);
    }

    private SysUserVo toVo(SysUserDto dto) {
        SysUserVo vo = new SysUserVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
