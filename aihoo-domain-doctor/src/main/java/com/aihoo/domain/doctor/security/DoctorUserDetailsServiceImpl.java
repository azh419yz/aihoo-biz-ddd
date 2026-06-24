package com.aihoo.domain.doctor.security;

import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.mapper.DoctorUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;

public class DoctorUserDetailsServiceImpl implements UserDetailsService {

    private final DoctorUserMapper doctorUserMapper;

    public DoctorUserDetailsServiceImpl(DoctorUserMapper doctorUserMapper) {
        this.doctorUserMapper = doctorUserMapper;
    }

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
