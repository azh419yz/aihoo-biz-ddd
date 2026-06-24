package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.dto.DoctorDirectoryDto;
import com.aihoo.domain.doctor.entity.DoctorDirectory;
import com.aihoo.domain.doctor.mapper.DoctorDirectoryMapper;
import com.aihoo.domain.doctor.service.DoctorDirectoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class DoctorDirectoryServiceImpl extends ServiceImpl<DoctorDirectoryMapper, DoctorDirectory> implements DoctorDirectoryService {

    @Override
    public List<DoctorDirectoryDto> findDoctorDirectoryList(String sickName) {
        IPage<DoctorDirectory> page = new Page<>(1, 50);
        QueryWrapper<DoctorDirectory> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(sickName), "sick_name", sickName);
        queryWrapper.eq("doctor_id", 42);
        queryWrapper.orderByDesc("create_time");
        List<DoctorDirectory> list = baseMapper.selectPage(page, queryWrapper).getRecords();

        return list.stream().map(dir -> {
            DoctorDirectoryDto dto = new DoctorDirectoryDto();
            dto.setSource(dir.getSource());
            dto.setSickId(dir.getSickId());
            dto.setPatientUserId(dir.getPatientUserId());
            return dto;
        }).toList();
    }
}
