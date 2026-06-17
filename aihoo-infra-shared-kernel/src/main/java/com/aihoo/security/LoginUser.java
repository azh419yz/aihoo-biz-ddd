package com.aihoo.security;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用登录用户上下文（避免 domain 层直接依赖 DoctorUser 等具体用户实体）
 */
@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 用户ID（医生/患者/管理员，跨域通用） */
    private String id;
    /** 用户姓名 */
    private String name;
    /** 用户类型：DOCTOR / PATIENT / ADMIN */
    private String type;
}
