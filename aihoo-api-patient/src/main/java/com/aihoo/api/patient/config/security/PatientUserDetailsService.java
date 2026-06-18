package com.aihoo.api.patient.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Patient API 的 UserDetailsService 桩。
 *
 * Patient 端走 phoneLogin + Redis token 流程，不通过 AuthenticationManager + UserDetailsService 登录。
 * 但 sys 域 SysUserServiceImpl 注入了 UserDetailsService（admin 业务用），
 * Spring 启动实例化时必须找到该 bean。
 *
 * 实际运行时本类永远不会被调用（patient 没有 admin 的 loginByPhone 流程），
 * 仅用于满足 Spring 容器 bean 依赖。
 */
@Service
public class PatientUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Patient API 不支持 UserDetailsService 登录");
    }
}
