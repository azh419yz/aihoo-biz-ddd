package com.aihoo.domain.sys.security;

import com.aihoo.domain.sys.entity.SysUser;
import com.aihoo.domain.sys.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;

public class SysUserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    public SysUserDetailsServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("user_name", username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new SysUserDetails(user, new HashSet<>());
    }
}
