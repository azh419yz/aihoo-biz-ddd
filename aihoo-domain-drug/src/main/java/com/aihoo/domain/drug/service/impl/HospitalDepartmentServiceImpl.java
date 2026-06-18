package com.aihoo.domain.drug.service.impl;

import com.aihoo.domain.drug.entity.HospitalDepartment;
import com.aihoo.domain.drug.mapper.HospitalDepartmentMapper;
import com.aihoo.domain.drug.service.HospitalDepartmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Classname HospitalDepartmentServiceImpl
 * @Description hf
 * @Date 2020/9/18 17:04
 * @Created by ad
 */
@Service
public class HospitalDepartmentServiceImpl extends ServiceImpl<HospitalDepartmentMapper, HospitalDepartment> implements HospitalDepartmentService {

    @Override
    public String findDepartmentNameByCode(String code) {
        java.util.List<HospitalDepartment> list = this.baseMapper.selectList(
                new QueryWrapper<HospitalDepartment>().eq("depart_code", code).last("LIMIT 1"));
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0).getDepartName();
    }
}
