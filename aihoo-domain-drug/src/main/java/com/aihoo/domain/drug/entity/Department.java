package com.aihoo.domain.drug.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础科室表（t_department）。
 *
 * <p>原 aihoo-biz-service/aihoo-admin 的 Department 实体，迁至 drug 域。
 * <p>与 HospitalDepartment（t_hospital_department，医院-科室关联）不同。
 */
@Data
@TableName("t_department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    /**
     * 1=一级科室 2=二级科室
     */
    private String level;

    /**
     * 科室图标
     */
    private String iconImg;

    /**
     * 科室编码
     */
    private String code;

    /**
     * 科室名称
     */
    private String name;

    /**
     * 父级编码
     */
    private String parentCode;

    /**
     * 是否展示
     */
    private String isShow;

    @TableField("`index`")
    private String index;

    /**
     * 状态
     */
    private String status;
}
