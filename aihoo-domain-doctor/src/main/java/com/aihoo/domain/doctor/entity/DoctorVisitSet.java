package com.aihoo.domain.doctor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 问诊设置表
 */
@Data
@TableName("t_doctor_visit_set")
public class DoctorVisitSet implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 医生id
     */
    private String doctorUserId;

    /**
     * 是否开启图文问诊 0-未开 1-开启
     */
    private Integer isImg;

    /**
     * 图文问诊价格
     */
    private Integer imgPrice;

    /**
     * 接单上限
     */
    private Integer upperLimit;

    /**
     * 是否开启免打扰时段
     */
    private Integer isDisturb;

    /**
     * 免打扰时段
     */
    private String noDisturbTime;

}
