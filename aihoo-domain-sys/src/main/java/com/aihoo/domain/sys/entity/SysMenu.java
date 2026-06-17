package com.aihoo.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
