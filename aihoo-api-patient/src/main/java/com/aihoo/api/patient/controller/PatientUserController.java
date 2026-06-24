package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.PhoneCodeRequest;
import com.aihoo.api.patient.request.PhoneRequest;
import com.aihoo.api.patient.request.WeChatPhoneRequest;
import com.aihoo.api.patient.vo.PatientUserApiVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.order.service.MdtOrderService;
import com.aihoo.domain.patient.entity.PatientUser;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.patient.service.PatientUserService;
import com.aihoo.domain.visit.service.HosVisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PatientUserV2", description = "患者端-患者相关接口")
@RestController
@RequestMapping("/api/v2/patientUser")
@RequiredArgsConstructor
@Slf4j
public class PatientUserController {
    private final PatientUserService patientUserService;
    private final MdtOrderService mdtOrderService;
    private final HosSickService hosSickService;
    private final HosVisitService hosVisitService;

    @GetMapping("/weChatLogin")
    @Operation(summary = "获取微信登录接口", description = "获取微信登录接口")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PatientUserApiVo.class},
                            description = "获取微信登录接口"
                    )
            )
    )
    public BizResult<PatientUserApiVo> weChatGetOpenId(@Parameter(name = "code", description = "临时登录凭证", example = "weixincode123456") @RequestParam(name = "code", required = true) String code) {
        PatientUser patientUser = patientUserService.weChatLogin(code);
        return BizResult.success(toVo(patientUser));
    }

    @PostMapping("/allowPrivacyPolicy")
    @Operation(summary = "授权隐私协议")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PatientUserApiVo.class},
                            description = "授权隐私协议"
                    )
            )
    )
    public BizResult<PatientUserApiVo> allowPrivacyPolicy() {
        PatientUser patientUser = patientUserService.allowPrivacyPolicy();
        return BizResult.success(toVo(patientUser));
    }

    @PostMapping("/checkPhone")
    @Operation(summary = "检查手机号码是否被占用接口")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "检查手机号码"
                    )
            )
    )
    public BizResult<Void> checkPhone(@Valid @RequestBody PhoneRequest request) {
        boolean checked = patientUserService.checkPhone(request.getMobile());
        if (checked) {
            return BizResult.success("手机号可以使用");
        } else {
            return BizResult.fail(BizResultCode.PATIENT_MOBILE_EXIST);
        }
    }

    @PostMapping("/bindWeChatPhone")
    @Operation(summary = "绑定微信手机号码接口")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "绑定微信手机号码"
                    )
            )
    )
    public BizResult<Void> bindWeChatPhone(@Valid @RequestBody WeChatPhoneRequest request) {
        patientUserService.bindWeChatPhone(request.getCode());
        return BizResult.success();
    }

    @PostMapping("/checkWeChatPhone")
    @Operation(summary = "校验微信手机号码接口")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "校验微信手机号码"
                    )
            )
    )
    public BizResult<Void> checkWeChatPhone(@Valid @RequestBody WeChatPhoneRequest request) {
        boolean checked = patientUserService.checkWeChatPhone(request.getCode());
        if (checked) {
            return BizResult.success("手机号可以使用");
        } else {
            return BizResult.fail(BizResultCode.PATIENT_MOBILE_EXIST);
        }
    }

    @PostMapping("/updatePhone")
    @Operation(summary = "修改手机号码接口")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "修改手机号码"
                    )
            )
    )
    public BizResult<Void> updatePhone(@Valid @RequestBody PhoneCodeRequest request) {
        patientUserService.updatePhone(request.getMobile(), request.getCode());
        return BizResult.success();
    }

    @GetMapping("/queryPatientUserById")
    @Operation(summary = "查询当前用户信息接口")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PatientUserApiVo.class},
                            description = "查询用户信息"
                    )
            )
    )
    public BizResult<PatientUserApiVo> queryPatientUser() {
        PatientUser patientUser = patientUserService.queryPatientUserById();
        return BizResult.success(toVo(patientUser));
    }

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
        boolean result = patientUserService.sendCode(request.getMobile());
        if (result) {
            return BizResult.success("验证码已发送");
        } else {
            return BizResult.fail(BizResultCode.SMS_SEND_ERROR);
        }
    }

    
    private PatientUserApiVo toVo(PatientUser patientUser) {
        if (patientUser == null) return null;
        PatientUserApiVo vo = new PatientUserApiVo();
        BeanUtils.copyProperties(patientUser, vo);
        vo.setOrderCount(mdtOrderService.countOrderByPatientUserId(patientUser.getId()));
        vo.setHosSickCount(hosSickService.countHosSickByPatientUserId(patientUser.getId()));
        vo.setVisitCount(hosVisitService.countHosVisitByPatientUserId(patientUser.getId()));
        vo.setAllowPrivacyPolicy("PASS".equals(patientUser.getIsAuth()));
        return vo;
    }
}