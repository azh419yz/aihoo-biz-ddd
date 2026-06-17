package com.aihoo.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员用户表（迁自 admin/system/model/SysUser）。
 */
@Data
@TableName("t_sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
}
