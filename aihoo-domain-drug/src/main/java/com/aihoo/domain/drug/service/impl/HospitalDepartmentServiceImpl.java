package com.aihoo.domain.drug.service.impl;

import com.aihoo.domain.drug.entity.HospitalDepartment;
import com.aihoo.domain.drug.mapper.HospitalDepartmentMapper;
import com.aihoo.domain.drug.service.HospitalDepartmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalDepartmentServiceImpl extends ServiceImpl<HospitalDepartmentMapper, HospitalDepartment> implements HospitalDepartmentService {

    @Override
    public String findDepartmentNameByCode(String code) {
        List<HospitalDepartment> list = this.baseMapper.selectList(
                new QueryWrapper<HospitalDepartment>().eq("depart_code", code).last("LIMIT 1"));
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0).getDepartName();
    }

    @Override
    public List<HospitalDepartment> findDepartCodeAllByHospitalId(String hospitalId) {
        return baseMapper.selectList(new QueryWrapper<HospitalDepartment>().eq("hospital_id", hospitalId));
    }

    @Override
    public void deleteByDepartCodes(List<String> departCodes, String hospitalId) {
        if (departCodes == null || departCodes.isEmpty()) {
            return;
        }
        baseMapper.delete(new QueryWrapper<HospitalDepartment>()
                .eq("hospital_id", hospitalId)
                .in("depart_code", departCodes));
    }
}
