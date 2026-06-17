package com.aihoo.domain.patient.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.common.BizResultCode;
import com.aihoo.constant.ImUserPrefix;
import com.aihoo.domain.order.service.MdtOrderService;
import com.aihoo.domain.patient.dto.PatientUserVo;
import com.aihoo.domain.patient.entity.PatientUser;
import com.aihoo.domain.patient.entity.PatientUserLog;
import com.aihoo.domain.patient.mapper.PatientUserLogMapper;
import com.aihoo.domain.patient.mapper.PatientUserMapper;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.patient.service.PatientUserService;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.exception.BizException;
import com.aihoo.properties.TencentProperties;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.ImUtils;
import com.aihoo.util.JSONUtil;
import com.aihoo.wechat.WeChatComponent;
import com.aihoo.wechat.dto.WeChatAccessTokenDTO;
import com.aihoo.wechat.dto.WeChatMobileDTO;
import com.aihoo.wechat.dto.WeChatSessionDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 患者用户 service 实现（迁自 patient-api 的 PatientUserServiceImpl，
 * 仅保留 api-patient controller 实际调用的方法）。
 */
@Service
@RequiredArgsConstructor
public class PatientUserServiceImpl extends ServiceImpl<PatientUserMapper, PatientUser> implements PatientUserService {

    private final PatientUserMapper patientUserMapper;
    private final PatientUserLogMapper patientUserLogMapper;
    private final MdtOrderService mdtOrderService;
    private final HosSickService hosSickService;
    private final HosVisitService hosVisitService;
    private final RedisService redisService;
    private final WeChatComponent weChatComponent;
    private final AliCloudComponent aliCloudComponent;
    private final TencentProperties tencentProperties;

    @Autowired
    private HttpServletRequest request;

    @Override
    public PatientUserVo weChatLogin(String code) {
        String openid = "", sessionKey = "", oldToken = "";
        //TODO: 模拟登录
        if ("1234".equals(code)) {
            openid = "oT6JW1--IGBPD6sFE2SR41aEpfT8";
            sessionKey = "123456";
        } else {
            WeChatSessionDTO weChatSession = weChatComponent.getWeChatSession(code);
            openid = weChatSession.getOpenid();
            sessionKey = weChatSession.getSessionKey();
        }

        String accessToken = UUID.randomUUID().toString().replace("-", "");
        String redisKey = RedisConstant.PATIENT_LOGIN_KEY + accessToken;

        PatientUser patientUser = null;
        if (Objects.nonNull(openid) && !openid.isBlank()) {
            patientUser = patientUserMapper.selectOne(new LambdaQueryWrapper<PatientUser>()
                    .eq(PatientUser::getWechatOpenId, openid));
        }
        if (Objects.isNull(patientUser)) {
            patientUser = new PatientUser();
            patientUser.setWechatOpenId(openid);
            patientUser.setSessionKey(sessionKey);
            patientUser.setToken(accessToken);
            patientUserMapper.insert(patientUser);
            String imUserId = String.format(ImUserPrefix.USER_ID_FORMAT, ImUserPrefix.PATIENT, patientUser.getId());
            String imUserSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
            patientUser.setImUserId(imUserId);
            patientUser.setImUserSig(imUserSig);
            patientUserMapper.updateById(patientUser);
        } else {
            oldToken = Objects.nonNull(patientUser.getToken()) && !patientUser.getToken().isBlank() ? patientUser.getToken() : "";
            LambdaUpdateWrapper<PatientUser> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(PatientUser::getToken, accessToken);
            updateWrapper.set(PatientUser::getSessionKey, sessionKey);
            updateWrapper.eq(PatientUser::getId, patientUser.getId());
            patientUserMapper.update(updateWrapper);
        }

        patientUser = patientUserMapper.selectById(patientUser.getId());
        redisSetToken(oldToken, redisKey, patientUser, StringUtils.isNotEmpty(oldToken));

        return toPatientUserVo(patientUser);
    }

    //缓存token
    private void redisSetToken(String oldToken, String redisKey, PatientUser patientUser, boolean notEmpty) {
        redisService.set(redisKey, patientUser, RedisConstant.TOKEN_SURVIVE_TIME);
        if (notEmpty) {
            redisService.remove(RedisConstant.PATIENT_LOGIN_KEY + oldToken);
        }
    }

    @Override
    public void updatePhone(String mobile) {
        String userId = AuthUtil.getLoginUserId();
        // 把原手机号置空
        LambdaUpdateWrapper<PatientUser> clearWrapper = new LambdaUpdateWrapper<>();
        clearWrapper.ne(PatientUser::getId, userId);
        clearWrapper.eq(PatientUser::getMobile, mobile);
        clearWrapper.set(PatientUser::getMobile, null);
        patientUserMapper.update(clearWrapper);

        // 修改当前用户的手机号
        LambdaUpdateWrapper<PatientUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PatientUser::getId, userId);
        wrapper.set(PatientUser::getMobile, mobile);
        patientUserMapper.update(wrapper);

        PatientUser loginUser = patientUserMapper.selectById(userId);
        if (loginUser != null && loginUser.getToken() != null) {
            String redisKey = RedisConstant.PATIENT_LOGIN_KEY + loginUser.getToken();
            redisService.set(redisKey, loginUser, RedisConstant.TOKEN_SURVIVE_TIME);
        }

        PatientUserLog userLog = new PatientUserLog();
        userLog.setActionType("BIND");
        userLog.setPatientUserId(userId);
        patientUserLogMapper.insert(userLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePhone(String mobile, String code) {
        String sendCodeKey = "send_code_" + mobile;
        if (!redisService.exists(sendCodeKey)) {
            throw new BizException(BizResultCode.SMS_CODE_EXPIRED);
        }
        Object codeObj = redisService.get(sendCodeKey);
        String codes = codeObj == null ? "" : codeObj.toString();
        if (!codes.equals(code)) {
            throw new BizException(BizResultCode.SMS_CODE_ERROR);
        }
        updatePhone(mobile);
        redisService.remove(sendCodeKey);
    }

    @Override
    public PatientUserVo queryPatientUserById() {
        String userId = AuthUtil.getLoginUserId();
        PatientUser patientUser = patientUserMapper.selectById(userId);
        return toPatientUserVo(patientUser);
    }

    @Override
    public boolean sendCode(String mobile) {
        String code = RandomUtil.randomNumbers(6);
        Map<String, String> template = Map.of("code", code);
        boolean result = aliCloudComponent.sendSms(mobile, JSONUtil.toJson(template));
        if (result) {
            String key = "send_code_" + mobile;
            redisService.set(key, code, 180);
        }
        return result;
    }

    @Override
    public boolean checkPhone(String mobile) {
        String loginUserId = AuthUtil.getLoginUserId();
        LambdaQueryWrapper<PatientUser> queryWrapper = new LambdaQueryWrapper<PatientUser>()
                .eq(PatientUser::getMobile, mobile)
                .ne(PatientUser::getId, loginUserId);
        return patientUserMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public void bindWeChatPhone(String code) {
        Object phoneObj = redisService.get("wechat_phone_" + code);
        String phoneNumber = phoneObj == null ? null : phoneObj.toString();
        if (phoneNumber == null || phoneNumber.isBlank()) {
            // 缓存失效，临时走 wechat API
            WeChatMobileDTO weChatMobile = getWeChatMobile(code);
            phoneNumber = weChatMobile.getPhoneInfo().getPurePhoneNumber();
        }
        updatePhone(phoneNumber);
    }

    @Override
    public boolean checkWeChatPhone(String code) {
        WeChatMobileDTO weChatMobile = getWeChatMobile(code);
        String phoneNumber = weChatMobile.getPhoneInfo().getPurePhoneNumber();
        redisService.set("wechat_phone_" + code, phoneNumber, 60 * 5);
        return checkPhone(phoneNumber);
    }

    private WeChatMobileDTO getWeChatMobile(String code) {
        String accessTokenKey = "wechat_access_token";
        String accessToken;
        if (redisService.exists(accessTokenKey)) {
            accessToken = redisService.get(accessTokenKey).toString();
        } else {
            WeChatAccessTokenDTO weChatAccessToken = weChatComponent.getWeChatAccessToken();
            accessToken = weChatAccessToken.getAccessToken();
            redisService.set(accessTokenKey, accessToken, weChatAccessToken.getExpiresIn());
        }
        return weChatComponent.getWeCHatMobile(accessToken, code);
    }

    @Override
    public PatientUserVo allowPrivacyPolicy() {
        String userId = AuthUtil.getLoginUserId();
        LambdaUpdateWrapper<PatientUser> updateWrapper = new LambdaUpdateWrapper<PatientUser>()
                .eq(PatientUser::getId, userId)
                .set(PatientUser::getIsAuth, "PASS");
        patientUserMapper.update(updateWrapper);

        PatientUser patientUser = patientUserMapper.selectById(userId);
        PatientUserVo vo = toPatientUserVo(patientUser);
        vo.setAllowPrivacyPolicy(Boolean.TRUE);
        return vo;
    }

    private PatientUserVo toPatientUserVo(PatientUser patientUser) {
        if (patientUser == null) {
            return null;
        }
        PatientUserVo vo = new PatientUserVo();
        BeanUtils.copyProperties(patientUser, vo);
        vo.setOrderCount(mdtOrderService.countOrderByPatientUserId(patientUser.getId()));
        vo.setHosSickCount(hosSickService.countHosSickByPatientUserId(patientUser.getId()));
        vo.setVisitCount(hosVisitService.countHosVisitByPatientUserId(patientUser.getId()));
        vo.setAllowPrivacyPolicy("PASS".equals(patientUser.getIsAuth()));
        return vo;
    }
}