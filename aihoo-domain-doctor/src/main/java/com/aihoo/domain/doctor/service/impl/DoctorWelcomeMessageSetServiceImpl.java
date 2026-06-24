package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.entity.DoctorWelcomeMessageSet;
import com.aihoo.domain.doctor.mapper.DoctorWelcomeMessageSetMapper;
import com.aihoo.domain.doctor.service.DoctorWelcomeMessageSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorWelcomeMessageSetServiceImpl
        extends ServiceImpl<DoctorWelcomeMessageSetMapper, DoctorWelcomeMessageSet>
        implements DoctorWelcomeMessageSetService {
}
