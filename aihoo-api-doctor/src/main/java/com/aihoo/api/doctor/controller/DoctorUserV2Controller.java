package com.aihoo.api.doctor.controller;


import com.aihoo.api.doctor.controller.request.PhoneCodeRequest;
import com.aihoo.api.doctor.controller.request.PhoneRequest;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.doctor.dto.DoctorUserVo;
import com.aihoo.domain.doctor.dto.DoctorVisitSetRequest;
import com.aihoo.domain.doctor.dto.DoctorWelcomeMessageRequest;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.model.entity.DoctorWelcomeMessageSet;
import com.aihoo.domain.doctor.service.DoctorUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 医生用户表 前端控制器
 * </p>
 *
 * @since 2020-09-15
 */
@Tag(name = "DoctorUserV2", description = "医生端-医生相关接口")
@RestController
@RequestMapping("/api/v2/doctorUser")
@RequiredArgsConstructor
public class DoctorUserV2Controller {

    private final DoctorUserService doctorUserService;


    @PostMapping("/sendCode")
    @Operation(summary = "获取验证码")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "获取验证码"
                    )
            )
    )
    public BizResult<Void> sendCode(@Valid @RequestBody PhoneRequest request) {

        //数据库中查询不到相对应的手机号码
        DoctorUser doctorUser = doctorUserService.selectMobile(request.getMobile());
        if (null == doctorUser) {
            return BizResult.fail(BizResultCode.DOCTOR_MOBILE_NOT_BOUND);
        }
        if (!"PASS".equals(doctorUser.getIsAuth())) {
            return BizResult.fail(BizResultCode.DOCTOR_ACCOUNT_NO_AUTH);
        }
        if ("0".equals(doctorUser.getStatus())) {
            return BizResult.fail(BizResultCode.DOCTOR_ACCOUNT_DISABLED);
        }
        boolean result = doctorUserService.sendCode(request.getMobile());
        if (result) {
            return BizResult.success("验证码已发送");
        } else {
            return BizResult.fail(BizResultCode.SMS_SEND_ERROR);
        }
    }


    @PostMapping("/phoneLogin")
    @Operation(summary = "手机验证码登录")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, DoctorUserVo.class},
                            description = "手机验证码登录"
                    )
            )
    )
    public BizResult<DoctorUserVo> login(@Valid @RequestBody PhoneCodeRequest request, HttpServletRequest servletRequest) {
        return BizResult.success(doctorUserService.phoneLogin(request.getMobile(), request.getCode(), servletRequest));
    }

    @GetMapping("/set")
    @Operation(summary = "查询问诊设置")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, DoctorVisitSet.class},
                            description = "查询问诊设置"
                    )
            )
    )
    public BizResult<DoctorVisitSet> getVisitSet() {
        return BizResult.success(doctorUserService.getVisitSet());
    }

    @PostMapping("/set")
    @Operation(summary = "问诊设置")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, DoctorVisitSet.class},
                            description = "问诊设置"
                    )
            )
    )
    public BizResult<DoctorVisitSet> setVisit(@Valid @RequestBody DoctorVisitSetRequest request) {
        return BizResult.success(doctorUserService.setVisit(request));
    }

    @GetMapping("/welcomeMessage")
    @Operation(summary = "查询欢迎语设置")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, DoctorWelcomeMessageSet.class},
                            description = "查询欢迎语设置"
                    )
            )
    )
    public BizResult<DoctorWelcomeMessageSet> getWelcomeMessageSet() {
        return BizResult.success(doctorUserService.getWelcomeMessage());
    }

    @PostMapping("/welcomeMessage")
    @Operation(summary = "欢迎语设置")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, DoctorWelcomeMessageSet.class},
                            description = "欢迎语设置"
                    )
            )
    )
    public BizResult<DoctorWelcomeMessageSet> setWelcomeMessage(@Valid @RequestBody DoctorWelcomeMessageRequest request) {
        return BizResult.success(doctorUserService.setWelcomeMessage(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "医详情")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, DoctorUserVo.class},
                            description = "医详情"
                    )
            )
    )
    public BizResult<DoctorUserVo> detail(@PathVariable String id) {
        return BizResult.success(doctorUserService.detail(id));
    }
}
