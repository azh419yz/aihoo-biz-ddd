package com.aihoo.domain.sys.dto;

import lombok.Data;

@Data
public class SysMenuDto {
    private String id;
    private String menuName;
    private String permission;
    private String menuUrl;
    private String parentId;
    private String isMenu;
    private String orderNumber;
    private String menuIcon;
    private String createdDate;
    private String updatedDate;
    private String deleted;
}
