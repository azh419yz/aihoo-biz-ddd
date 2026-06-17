package com.aihoo.domain.sys.dto;

import lombok.Data;

@Data
public class SysRoleDto {
    private String id;
    private String roleName;
    private String comments;
    private String deleted;
    private String createdDate;
    private String updatedDate;
    private String createUser;
}
