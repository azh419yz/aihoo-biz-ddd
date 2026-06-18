package com.aihoo.domain.drug.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Classname Hospital
 * @Description 医院实体
 * @Date 2020/9/16 20:32
 * @Created by ad
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_hospital")
public class Hospital implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String hospitalNo;

    private String createTime;

    private String updateTime;

    private String createUserId;

    private String hosName;

    private String hosMobile;

    private String hosGradeCode;

    private String hosGradeName;

    private String hosLevelCode;

    private String hosLevelName;

    private String hosCateCode;

    private String hosCateName;

    private String hosAttCode;

    private String hosAttName;

    private String provinceCode;

    private String cityCode;

    private String districtCode;

    private String province;

    private String city;

    private String district;

    private String address;

    private String imgs;

    private String status;

    private String content;

    private Integer isDelete;
}
