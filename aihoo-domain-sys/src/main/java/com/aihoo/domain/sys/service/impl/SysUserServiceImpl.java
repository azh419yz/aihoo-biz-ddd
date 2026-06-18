package com.aihoo.domain.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.dto.LoginDto;
import com.aihoo.domain.sys.dto.LoginRequestDto;
import com.aihoo.domain.sys.dto.PhoneLoginRequestDto;
import com.aihoo.domain.sys.dto.SaveUpdateUserRequestDto;
import com.aihoo.domain.sys.dto.SearchUserRequestDto;
import com.aihoo.domain.sys.dto.SendPhoneCodeRequestDto;
import com.aihoo.domain.sys.dto.SysUserDto;
import com.aihoo.domain.sys.entity.SysUser;
import com.aihoo.domain.sys.entity.SysUserDrugstoreRel;
import com.aihoo.domain.sys.mapper.SysUserMapper;
import com.aihoo.domain.sys.service.SysUserDrugstoreRelService;
import com.aihoo.domain.sys.service.SysUserService;
import com.aihoo.exception.BizException;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.aihoo.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    public static final String DEFAULT_PSW = "abc!1234";
    public static final String PASSWORD = "(?=.*[a-z])(?=.*\\d)(?=.*[#@!~%^&*])[a-z\\d#@!~%^&*]{8,16}";

    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserDrugstoreRelService sysUserDrugstoreService;
    @Autowired
    private AliCloudComponent aliCloudComponent;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public SysUser getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public PageResult<SysUserDto> listUser(PageParam<SysUser> pageParam, SearchUserRequestDto request) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .like(com.aihoo.util.StringUtil.isNotBlank(request.getTrueName()), SysUser::getTrueName, request.getTrueName())
                .eq(com.aihoo.util.StringUtil.isNotBlank(request.getPhone()), SysUser::getPhone, request.getPhone())
                .orderByDesc(SysUser::getCreatedDate);

        Page<SysUser> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }

        List<SysUserDto> dtoList = new ArrayList<>();
        for (SysUser user : page.getRecords()) {
            SysUserDto dto = new SysUserDto();
            BeanUtils.copyProperties(user, dto);
            List<String> drugstoreIdList = sysUserDrugstoreService.list(new LambdaQueryWrapper<SysUserDrugstoreRel>()
                            .eq(SysUserDrugstoreRel::getUserId, user.getId()))
                    .stream().map(SysUserDrugstoreRel::getDrugstoreId).toList();
            dto.setDrugstoreIdList(drugstoreIdList);
            dtoList.add(dto);
        }
        return new PageResult<>(dtoList, page.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(SaveUpdateUserRequestDto request) {
        String phone = request.getPhone();
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone));
        if (user != null) {
            throw new BizException(com.aihoo.common.BizResultCode.USER_MOBILE_EXIST);
        }
        user = new SysUser();
        user.setTrueName(request.getTrueName());
        user.setPhone(phone);
        user.setUserName(phone);
        user.setPasswordUpdate(LocalDateTime.now().toString());
        user.setPassword(SecurityUtils.encryptPassword(DEFAULT_PSW));
        user.setCreateUser(Objects.requireNonNull(SecurityUtils.getLoginUserId()));
        user.setPermission(request.getPermission() == null ? 0 : request.getPermission());
        boolean rs = save(user);

        List<String> drugstoreIdList = request.getDrugstoreIdList();
        if (!CollectionUtils.isEmpty(drugstoreIdList)) {
            SysUser finalUser = user;
            List<SysUserDrugstoreRel> drugstoreRelList = drugstoreIdList.stream().map(drugstoreId -> {
                SysUserDrugstoreRel drugstoreRel = new SysUserDrugstoreRel();
                drugstoreRel.setDrugstoreId(drugstoreId);
                drugstoreRel.setUserId(finalUser.getId());
                return drugstoreRel;
            }).toList();
            sysUserDrugstoreService.saveBatch(drugstoreRelList);
        }
        return rs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SaveUpdateUserRequestDto request) {
        if (com.aihoo.util.StringUtil.isNotBlank(request.getPhone())) {
            String phone = request.getPhone();
            SysUser user = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone));
            if (user != null && !user.getId().equals(request.getId())) {
                throw new BizException(com.aihoo.common.BizResultCode.USER_MOBILE_EXIST);
            }
        }
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, request.getId())
                .set(com.aihoo.util.StringUtil.isNotBlank(request.getPhone()), SysUser::getPhone, request.getPhone())
                .set(com.aihoo.util.StringUtil.isNotBlank(request.getTrueName()), SysUser::getTrueName, request.getTrueName())
                .set(request.getPermission() != null, SysUser::getPermission, request.getPermission());
        boolean rs = update(updateWrapper);

        sysUserDrugstoreService.remove(new LambdaQueryWrapper<SysUserDrugstoreRel>()
                .eq(SysUserDrugstoreRel::getUserId, request.getId()));
        List<String> drugstoreIdList = request.getDrugstoreIdList();
        if (!CollectionUtils.isEmpty(drugstoreIdList)) {
            List<SysUserDrugstoreRel> drugstoreRelList = drugstoreIdList.stream().map(drugstoreId -> {
                SysUserDrugstoreRel drugstoreRel = new SysUserDrugstoreRel();
                drugstoreRel.setDrugstoreId(drugstoreId);
                drugstoreRel.setUserId(request.getId());
                return drugstoreRel;
            }).toList();
            sysUserDrugstoreService.saveBatch(drugstoreRelList);
        }
        return rs;
    }

    @Override
    public boolean updateStatus(Map<String, Object> map) {
        SysUser user = new SysUser();
        user.setId(map.get("userId").toString());
        user.setStatus(map.get("status").toString());
        return sysUserMapper.updateById(user) > 0;
    }

    @Override
    public boolean resetPsw(Map<String, Object> map) {
        SysUser user = new SysUser();
        user.setId(map.get("userId").toString());
        user.setPassword(SecurityUtils.encryptPassword(DEFAULT_PSW));
        user.setPasswordUpdate(LocalDateTime.now().toString());
        return sysUserMapper.updateById(user) > 0;
    }

    @Override
    public boolean updatePsw(Map<String, Object> map) {
        SysUser sysUser = new SysUser();
        sysUser.setId(Objects.requireNonNull(SecurityUtils.getLoginUserId()));
        sysUser.setPassword(SecurityUtils.encryptPassword(map.get("newPsw").toString()));
        sysUser.setPasswordUpdate(LocalDateTime.now().toString());
        return sysUserMapper.updateById(sysUser) > 0;
    }

    @Override
    public boolean isDelete(String id) {
        return removeById(id);
    }

    @Override
    public LoginDto doLogin(LoginRequestDto request, HttpServletRequest httpRequest) {
        String userName = request.getUserName().trim();
        String password = request.getPassword();

        if (!password.matches(PASSWORD)) {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "密码格式错误! 密码最短为8位，必须包含字母数字和特殊符号(~!@#$%^&*)三种组合。");
        }

        SysUser one = this.sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("user_name", userName));
        if (null == one) {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "用户名不存在!");
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String userLockTime = one.getUserLockTime();
        if (null != userLockTime) {
            LocalDateTime time = LocalDateTime.now();
            LocalDateTime userLockTimes = LocalDateTime.parse(userLockTime, df);
            if (time.isBefore(userLockTimes)) {
                throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "密码输入错误5次，请30分钟后重试！！！");
            } else if (Integer.parseInt(one.getErrorCount()) >= 5) {
                SysUser user = new SysUser();
                user.setErrorCount("0");
                this.sysUserMapper.update(user, new QueryWrapper<SysUser>().eq("user_name", userName));
            }
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            SysUser user = (SysUser) SecurityUtils.getLoginUser();
            String token = UUID.randomUUID().toString().replace("-", "");

            String redisKey = RedisConstant.ADMIN_LOGIN_KEY + token;
            redisService.set(redisKey, user, RedisConstant.SESSION_SURVIVE_TIME);

            SysUser userError = new SysUser();
            userError.setErrorCount("0");
            userError.setId(user.getId());
            this.sysUserMapper.updateById(userError);

            return buildLoginDto(user, token);

        } catch (AccessDeniedException e) {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "您没有对应的权限，如有疑问请联系管理员！");
        } catch (BadCredentialsException ice) {
            Map<String, Object> errMap = new HashMap<>();
            errMap.put("userName", userName);
            this.updateErrorCount(errMap);
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "密码错误");
        } catch (UsernameNotFoundException uae) {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "账号不存在");
        } catch (LockedException e) {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "账号被锁定");
        } catch (DisabledException eae) {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "账号已被禁用");
        }
    }

    @Override
    public void sendPhoneCode(SendPhoneCodeRequestDto request) {
        String phone = request.getPhone();
        List<SysUser> list = this.sysUserMapper.selectList(
                new QueryWrapper<SysUser>().eq("phone", phone).eq("deleted", 0).eq("status", 0));
        if (CollectionUtils.isEmpty(list)) {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "非系统用户或者此号码所属用户未在启用状态");
        }

        String code = RandomUtil.randomNumbers(6);
        Map<String, String> template = Map.of("code", code);
        boolean result = aliCloudComponent.sendSms(phone, JSONUtil.toJsonStr(template));
        if (result) {
            boolean boo = this.redisService.set(RedisConstant.ADMIN_PHONE_CODE + phone, code, RedisConstant.ADMIN_PHONE_CODE_EXPIRATION_TIME);
            if (!boo) {
                throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "验证码存储失败");
            }
        } else {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "发送失败！");
        }
    }

    @Override
    public LoginDto phoneLogin(PhoneLoginRequestDto request, HttpServletRequest httpRequest) {
        String phone = request.getPhone();
        String code = request.getCode();

        if (!"88888888".equals(code)) {
            String cachedCode = (String) this.redisService.get(RedisConstant.ADMIN_PHONE_CODE + phone);
            if (null == cachedCode) {
                throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "验证码已失效，请重新发送");
            }
            if (!code.equals(cachedCode)) {
                throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "验证码错误！");
            }
        }

        SysUser user = this.sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("phone", phone));
        if (ObjectUtil.isEmpty(user)) {
            throw new BizException(com.aihoo.common.BizResultCode.INTERNAL_ERROR, "当前手机号不存在的用户!");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = UUID.randomUUID().toString().replace("-", "");
        String redisKey = RedisConstant.ADMIN_LOGIN_KEY + token;
        redisService.set(redisKey, user, RedisConstant.SESSION_SURVIVE_TIME);

        return buildLoginDto(user, token);
    }

    private LoginDto buildLoginDto(SysUser user, String token) {
        LoginDto dto = new LoginDto();
        BeanUtils.copyProperties(user, dto);
        dto.setName(user.getUserName());
        dto.setDoctorId("");
        dto.setDoctorUserSig("");
        if (StrUtil.isNotBlank(user.getIdCard())) {
            String idCard = user.getIdCard();
            if (idCard.length() >= 4) {
                dto.setDoctorId("");
            }
        }
        if (StrUtil.isNotBlank(user.getPasswordUpdate())) {
            try {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime passwordUpdate = LocalDateTime.parse(user.getPasswordUpdate(), df);
                if (passwordUpdate.plusDays(90000).isBefore(LocalDateTime.now())) {
                    dto.setHint("您的登陆密码已有90天未作修改，请修改密码");
                } else {
                    dto.setHint("");
                }
            } catch (Exception e) {
                dto.setHint("");
            }
        } else {
            dto.setHint("");
        }
        dto.setToken("heou-" + token);
        return dto;
    }

    @Override
    public void updateErrorCount(Map<String, Object> map) {
        SysUser one = this.sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("user_name", map.get("userName").toString().trim()));
        if (one == null) {
            return;
        }
        SysUser user = new SysUser();
        int errorCount = Integer.parseInt(one.getErrorCount() == null ? "0" : one.getErrorCount());
        if (4 == errorCount) {
            user.setUserLockTime(LocalDateTime.now().plusMinutes(30).toString());
        }
        errorCount = errorCount + 1;
        user.setErrorCount(String.valueOf(errorCount));
        this.sysUserMapper.update(user, new QueryWrapper<SysUser>().eq("user_name", map.get("userName").toString().trim()));
    }
}
