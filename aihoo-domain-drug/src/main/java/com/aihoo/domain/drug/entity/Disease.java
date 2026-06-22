package com.aihoo.domain.drug.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 疾病基础表（d_disease）。
 *
 * <p>原 aihoo-biz-service/aihoo-admin 的 Disease 实体，迁至 drug 域。
 * <p>仅供 admin 端管理使用，patient/doctor 阶段未引用。
 */
@Data
@TableName("d_disease")
public class Disease implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 疾病名称
     */
    private String name;

    /**
     * 疾病编码
     */
    private String code;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;
}
