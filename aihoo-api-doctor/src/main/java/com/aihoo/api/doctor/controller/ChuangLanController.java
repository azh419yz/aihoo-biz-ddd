package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.config.properties.ChuanglanProperties;
import com.aihoo.api.doctor.request.ChuangLanLoginRequest;
import com.aihoo.api.doctor.util.ChuangLanFlashAuthUtil;
import com.aihoo.api.doctor.util.CodeUtils;
import com.aihoo.api.doctor.vo.DoctorUserVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.doctor.dto.DoctorUserDto;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.domain.visit.service.PrescriptionService;
import com.aihoo.domain.visit.service.ProposalService;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 创蓝第三方接口
 * </p>
 *
 * @author wyz
 * @since 2026/3/4 16:32
 */
@Tag(name = "Chuanglan", description = "医生端-创蓝登录接口")
@RestController
@RequestMapping("/api/v2/chuanglan")
@RequiredArgsConstructor
@Log4j2
public class ChuangLanController {

    private final ChuanglanProperties chuanglanProperties;

    private final DoctorUserService doctorUserService;

    private final PrescriptionService prescriptionService;

    private final HosVisitService hosVisitService;

    private final ProposalService proposalService;

    @PostMapping("/login")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, DoctorUserVo.class},
                            description = "返回信息发送相关结果"
                    )
            )
    )
    @Operation(summary = "登录")
    public BizResult<DoctorUserVo> login(@RequestBody ChuangLanLoginRequest req, HttpServletRequest servletRequest) {
        if (req.getLoginType() == null || req.getLoginType() == 0
                || StringUtils.isEmpty(req.getToken()) || StringUtils.isEmpty(req.getSource())) {
            return BizResult.fail(500, "参数错误");
        }

        String appId = req.getSource().equals("IOS") ?
                chuanglanProperties.getIos().getAppId() :
                chuanglanProperties.getAndroid().getAppId();
        String appKey = req.getSource().equals("IOS") ?
                chuanglanProperties.getIos().getAppKey() :
                chuanglanProperties.getAndroid().getAppKey();
        Map<String, Object> result;
        if (req.getLoginType().equals(1)) {
            result = ChuangLanFlashAuthUtil.queryMobileApp(req.getToken(), "", "", appId, appKey);
        } else {
            result = ChuangLanFlashAuthUtil.validateMobileApp(req.getToken(), req.getMobile(), appId, appKey);
        }

        if (MapUtils.isNotEmpty(result)) {
            JSONObject loginResult = new JSONObject(result);
            log.info("请求创蓝接口返回数据:{}", loginResult.toString());
            if (!loginResult.getString("code").equals("200000")) {
                return BizResult.fail(401, "登录失败");
            }
            DoctorUserDto doctorUserDto = doctorUserService.loginUser(loginResult.getString("mobile"), servletRequest);
            if (doctorUserDto == null) {
                return BizResult.fail(401, "未找到对应医生账号或账号未通过认证");
            }
            return BizResult.success(convert2Vo(doctorUserDto));
        } else {
            return BizResult.fail(500, "创蓝接口请求失败");
        }
    }

    private DoctorUserVo convert2Vo(DoctorUserDto doctorUserDto) {
        DoctorUserVo userVo = new DoctorUserVo();
        BeanUtils.copyProperties(doctorUserDto, userVo);
        // 手机号
        userVo.setMobile(CodeUtils.stringSixMask(doctorUserDto.getMobile()));
        // 开方数
        userVo.setPrescriptionCount(prescriptionService.countByDoctorUserId(doctorUserDto.getId()));
        // 患者数
        userVo.setVisitCount(hosVisitService.countHostVisitByDoctor(doctorUserDto.getId()));
        // 评价数
        userVo.setProposalCount(proposalService.countByDoctorUserId(doctorUserDto.getId()));
        return userVo;
    }
}
