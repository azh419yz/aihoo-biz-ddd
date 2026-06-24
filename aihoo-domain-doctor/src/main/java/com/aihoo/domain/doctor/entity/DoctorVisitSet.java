package com.aihoo.domain.doctor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_doctor_visit_set")
public class DoctorVisitSet implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    
    private String createTime;

    
    private String updateTime;

    
    private String doctorUserId;

    
    private Integer isImg;

    
    private Integer imgPrice;

    
    private Integer upperLimit;

    
    private Integer isDisturb;

    
    private String noDisturbTime;

}
