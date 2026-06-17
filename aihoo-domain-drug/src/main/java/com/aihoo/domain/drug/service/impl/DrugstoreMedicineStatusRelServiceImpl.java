package com.aihoo.domain.drug.service.impl;

import com.aihoo.domain.drug.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.drug.mapper.DrugstoreMedicineStatusRelMapper;
import com.aihoo.domain.drug.service.DrugstoreMedicineStatusRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DrugstoreMedicineStatusRelServiceImpl
        extends ServiceImpl<DrugstoreMedicineStatusRelMapper, DrugstoreMedicineStatusRel>
        implements DrugstoreMedicineStatusRelService {
}
