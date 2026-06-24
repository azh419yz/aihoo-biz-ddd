package com.aihoo.domain.sys.service;

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
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    PageResult<SysUserDto> listUser(PageParam<SysUser> pageParam, SearchUserRequestDto request);

    boolean addUser(SaveUpdateUserRequestDto request);

    boolean update(SaveUpdateUserRequestDto request);

    boolean updateStatus(Map<String, Object> map);

    boolean resetPsw(Map<String, Object> map);

    boolean updatePsw(Map<String, Object> map);

    boolean isDelete(String id);

    void updateErrorCount(Map<String, Object> map);

    LoginDto doLogin(LoginRequestDto request, HttpServletRequest httpRequest);

    void sendPhoneCode(SendPhoneCodeRequestDto request);

    LoginDto phoneLogin(PhoneLoginRequestDto request, HttpServletRequest httpRequest);
}
