package com.aihoo.domain.doctor.security;

import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.mapper.DoctorUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * Doctor 端的 UserDetailsService。
 *
 * 当前医生端主要走 phoneLogin（手机号+验证码）流程，
 * 由 TokenAuthenticationFilter 通过 Redis token 完成认证。
 * 此处保留 UserDetailsService 实现以满足 Spring Security 6 启动要求
 * （AuthenticationManager 需要 UserDetailsService 后端）。
 *
 * 登录用户名约定为 doctorUser.mobile。
 */
@Service
public class DoctorUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private DoctorUserMapper doctorUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DoctorUser user = doctorUserMapper.selectByMobile(
                new LambdaQueryWrapper<DoctorUser>().eq(DoctorUser::getMobile, username));
        if (user == null) {
            throw new UsernameNotFoundException("Doctor not found with mobile: " + username);
        }
        return new DoctorUserDetails(user, new HashSet<>());
    }
}
