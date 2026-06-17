package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.entity.HosPrescriptionInstruction;
import com.aihoo.domain.visit.mapper.HosPrescriptionInstructionMapper;
import com.aihoo.domain.visit.service.PrescriptionInstructionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionInstructionServiceImpl extends ServiceImpl<HosPrescriptionInstructionMapper, HosPrescriptionInstruction> implements PrescriptionInstructionService {
}