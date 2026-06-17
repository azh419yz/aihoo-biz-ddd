package com.aihoo.api.doctor.config.security;

import com.aihoo.domain.doctor.entity.DoctorUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class DoctorLoginUser implements UserDetails {

    private DoctorUser doctorUser;
    private Set<String> permissions;

    public DoctorLoginUser(DoctorUser doctorUser, Set<String> permissions) {
        this.doctorUser = doctorUser;
        this.permissions = permissions;
    }

    public DoctorUser getDoctorUser() {
        return doctorUser;
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
        return "";
    }

    @Override
    public String getUsername() {
        return doctorUser.getMobile();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"1".equals(doctorUser.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !"1".equals(doctorUser.getIsCancel());
    }
}
