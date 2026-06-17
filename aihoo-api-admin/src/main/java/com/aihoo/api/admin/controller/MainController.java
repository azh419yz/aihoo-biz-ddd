package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.request.LoginRequest;
import com.aihoo.api.admin.request.PhoneLoginRequest;
import com.aihoo.api.admin.request.SendPhoneCodeRequest;
import com.aihoo.api.admin.vo.LoginVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.dto.LoginDto;
import com.aihoo.domain.sys.dto.LoginRequestDto;
import com.aihoo.domain.sys.dto.PhoneLoginRequestDto;
import com.aihoo.domain.sys.dto.SendPhoneCodeRequestDto;
import com.aihoo.domain.sys.service.SysMenuService;
import com.aihoo.domain.sys.service.SysUserService;
import com.aihoo.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Main", description = "后台基础核心接口")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {

    private final SysMenuService sysMenuService;
    private final SysUserService sysUserService;

    @Operation(summary = "获取当前登录用户的菜单及按钮")
    @PostMapping("/userMenuButton")
    public BizResult<List<Map<String, Object>>> index() {
        List<Map<String, Object>> menuTree = sysMenuService.userMenuButton(Integer.parseInt(SecurityUtils.getLoginUserId()));
        return BizResult.success(menuTree);
    }

    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
    public BizResult<LoginVo> doLogin(@Validated @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        LoginRequestDto dto = new LoginRequestDto();
        BeanUtils.copyProperties(request, dto);
        LoginVo vo = toVo(sysUserService.doLogin(dto, httpRequest));
        return BizResult.success(vo);
    }

    @Operation(summary = "手机号验证码登陆")
    @PostMapping("phone/login")
    public BizResult<LoginVo> phoneLogin(@Validated @RequestBody PhoneLoginRequest request, HttpServletRequest httpRequest) {
        PhoneLoginRequestDto dto = new PhoneLoginRequestDto();
        BeanUtils.copyProperties(request, dto);
        LoginVo vo = toVo(sysUserService.phoneLogin(dto, httpRequest));
        return BizResult.success(vo);
    }

    @Operation(summary = "登出系统")
    @PostMapping("/logout")
    public BizResult<Void> logout() {
        SecurityContextHolder.clearContext();
        return BizResult.success();
    }

    @Operation(summary = "获取手机验证码")
    @PostMapping("/getCode")
    public BizResult<Void> getCode(@Validated @RequestBody SendPhoneCodeRequest request) {
        SendPhoneCodeRequestDto dto = new SendPhoneCodeRequestDto();
        BeanUtils.copyProperties(request, dto);
        sysUserService.sendPhoneCode(dto);
        return BizResult.success();
    }

    private LoginVo toVo(LoginDto dto) {
        if (dto == null) return null;
        LoginVo vo = new LoginVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
