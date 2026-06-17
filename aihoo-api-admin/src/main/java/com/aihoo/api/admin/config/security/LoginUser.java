package com.aihoo.api.admin.config.security;

import com.aihoo.domain.sys.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class LoginUser implements UserDetails {

    private final SysUser sysUser;
    private final Set<String> permissions;

    public LoginUser(SysUser sysUser, Set<String> permissions) {
        this.sysUser = sysUser;
        this.permissions = permissions;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"1".equals(sysUser.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !"1".equals(sysUser.getStatus());
    }
}
