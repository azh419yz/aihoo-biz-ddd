package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.entity.DoctorDirectory;
import com.aihoo.domain.doctor.mapper.DoctorDirectoryMapper;
import com.aihoo.domain.doctor.service.DoctorDirectoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DoctorDirectoryServiceImpl extends ServiceImpl<DoctorDirectoryMapper, DoctorDirectory> implements DoctorDirectoryService {
}