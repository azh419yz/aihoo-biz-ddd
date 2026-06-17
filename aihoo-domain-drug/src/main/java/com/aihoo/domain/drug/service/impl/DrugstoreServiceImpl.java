package com.aihoo.domain.drug.service.impl;

import com.aihoo.domain.drug.entity.Drugstore;
import com.aihoo.domain.drug.mapper.DrugstoreMapper;
import com.aihoo.domain.drug.service.DrugstoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 药房 服务实现（迁自 patient-api 的 DrugstoreServiceImpl）。
 *
 * @author wyz
 * @since 2026/3/15
 */
@Service
public class DrugstoreServiceImpl extends ServiceImpl<DrugstoreMapper, Drugstore> implements DrugstoreService {

    @Override
    public List<Drugstore> selectDrugstoreByDrug() {
        return List.of();
    }
}
