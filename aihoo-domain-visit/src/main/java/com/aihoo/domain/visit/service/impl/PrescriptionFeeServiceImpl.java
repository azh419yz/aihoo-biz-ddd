package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.entity.HosPrescriptionFee;
import com.aihoo.domain.visit.mapper.HosPrescriptionFeeMapper;
import com.aihoo.domain.visit.service.PrescriptionFeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionFeeServiceImpl extends ServiceImpl<HosPrescriptionFeeMapper, HosPrescriptionFee> implements PrescriptionFeeService {
}