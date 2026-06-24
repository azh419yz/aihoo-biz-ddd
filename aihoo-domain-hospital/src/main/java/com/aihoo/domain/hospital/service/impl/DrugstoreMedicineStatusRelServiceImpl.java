package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.hospital.mapper.DrugstoreMedicineStatusRelMapper;
import com.aihoo.domain.hospital.service.DrugstoreMedicineStatusRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DrugstoreMedicineStatusRelServiceImpl
        extends ServiceImpl<DrugstoreMedicineStatusRelMapper, DrugstoreMedicineStatusRel>
        implements DrugstoreMedicineStatusRelService {
}
