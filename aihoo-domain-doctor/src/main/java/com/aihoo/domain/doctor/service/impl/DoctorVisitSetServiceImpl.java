package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.mapper.DoctorVisitSetMapper;
import com.aihoo.domain.doctor.service.DoctorVisitSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorVisitSetServiceImpl extends ServiceImpl<DoctorVisitSetMapper, DoctorVisitSet>
        implements DoctorVisitSetService {
}
