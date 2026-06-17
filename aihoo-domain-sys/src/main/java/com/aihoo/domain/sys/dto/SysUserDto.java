package com.aihoo.domain.sys.dto;

import lombok.Data;

import java.util.List;

@Data
public class SysUserDto {
    private String id;
    private String userSig;
    private String userName;
    private String password;
    private String nickName;
    private String avatar;
    private String sex;
    private String phone;
    private String email;
    private String emailVerified;
    private String trueName;
    private String idCard;
    private String birthday;
    private String departmentId;
    private String status;
    private String createdDate;
    private String updatedDate;
    private String deleted;
    private String createUser;
    private String errorCount;
    private String userLockTime;
    private String passwordUpdate;
    private Integer permission;

    private List<String> drugstoreIdList;
}
