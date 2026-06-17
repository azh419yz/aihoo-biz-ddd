package com.aihoo.api.admin.controller;

import com.aihoo.common.BaseController;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.model.dto.LoginRequest;
import com.aihoo.domain.sys.model.dto.PhoneLoginRequest;
import com.aihoo.domain.sys.model.dto.SendPhoneCodeRequest;
import com.aihoo.domain.sys.model.vo.LoginVo;
import com.aihoo.domain.sys.service.SysMenuService;
import com.aihoo.domain.sys.service.SysUserService;
import com.aihoo.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * MainController
 */
@Tag(name = "MainController", description = "后台基础核心接口")
@RestController
@RequestMapping("/api/v1")
public class MainController extends BaseController {
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysUserService sysUserService;


    // 当前登录用户的菜单以及按钮
    @Operation(summary = "获取当前登录用户的菜单及按钮")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, List.class},
                            description = "获取当前登录用户的菜单及按钮"
                    )
            )
    )
    @PostMapping("/userMenuButton")
    public BizResult<List<Map<String, Object>>> index() {
        List<Map<String, Object>> menuTree = sysMenuService.userMenuButton(Integer.parseInt(SecurityUtils.getLoginUserId()));
        return BizResult.success(menuTree);
    }


    // 登录
    @Operation(summary = "账号密码登录")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, LoginVo.class},
                            description = "账号密码登录"
                    )
            )
    )
    @PostMapping("/login")
    public BizResult<LoginVo> doLogin(@Validated @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        LoginVo vo = sysUserService.doLogin(request, httpRequest);
        return BizResult.success(vo);
    }

    //手机号验证码登陆
    @Operation(summary = "手机号验证码登陆")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, LoginVo.class},
                            description = "手机号验证码登陆"
                    )
            )
    )
    @PostMapping("phone/login")
    public BizResult<LoginVo> phoneLogin(@Validated @RequestBody PhoneLoginRequest request, HttpServletRequest httpRequest) {
        LoginVo vo = sysUserService.phoneLogin(request, httpRequest);
        return BizResult.success(vo);
    }


    @Operation(summary = "登出系统")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "登出系统"
                    )
            )
    )
    @PostMapping("/logout")
    public BizResult<Void> logout() {
        SecurityContextHolder.clearContext();
        return BizResult.success();
    }

    /**
     * 获取手机验证码
     *
     * @return code
     */
    @Operation(summary = "获取手机验证码")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "获取手机验证码"
                    )
            )
    )
    @PostMapping("/getCode")
    public BizResult<Void> getCode(@Validated @RequestBody SendPhoneCodeRequest request) {
        sysUserService.sendPhoneCode(request);
        return BizResult.success();
    }

}
