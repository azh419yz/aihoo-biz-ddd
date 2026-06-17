package com.aihoo.domain.drug.service.impl;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.dto.SearchDrugstoreRequestDto;
import com.aihoo.domain.drug.entity.Drugstore;
import com.aihoo.domain.drug.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.drug.entity.DrugstoreProvinceRel;
import com.aihoo.domain.drug.mapper.DrugstoreMapper;
import com.aihoo.domain.drug.service.DrugstoreMedicineStatusRelService;
import com.aihoo.domain.drug.service.DrugstoreProvinceRelService;
import com.aihoo.domain.drug.service.DrugstoreService;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 药房 服务实现（迁自 patient-api + doctor-api 的 DrugstoreServiceImpl）。
 *
 * @author wyz
 * @since 2026/3/15
 */
@Service
@RequiredArgsConstructor
public class DrugstoreServiceImpl extends ServiceImpl<DrugstoreMapper, Drugstore> implements DrugstoreService {

    private final DrugstoreProvinceRelService provinceRelService;
    private final DrugstoreMedicineStatusRelService medicineStatusRelService;

    @Override
    public List<Drugstore> selectDrugstoreByDrug() {
        return List.of();
    }

    @Override
    public PageResult<Drugstore> getPage(PageParam<Drugstore> pageParam, SearchDrugstoreRequestDto request) {
        List<String> idListByProvince = null;
        if (StringUtil.isNotBlank(request.getProvincesCode())) {
            idListByProvince = provinceRelService.list(new LambdaQueryWrapper<DrugstoreProvinceRel>()
                            .eq(DrugstoreProvinceRel::getProvinceCode, request.getProvincesCode()))
                    .stream()
                    .map(DrugstoreProvinceRel::getDrugstoreId)
                    .toList();
            if (CollectionUtils.isEmpty(idListByProvince)) {
                return new PageResult<>();
            }
        }

        List<String> idListByMedicineStatus = null;
        if (!CollectionUtils.isEmpty(request.getMedicineStatusList())) {
            idListByMedicineStatus = medicineStatusRelService.listObjs(new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                    .select(DrugstoreMedicineStatusRel::getDrugstoreId)
                    .in(DrugstoreMedicineStatusRel::getMedicineStatusCode, request.getMedicineStatusList())
                    .groupBy(DrugstoreMedicineStatusRel::getDrugstoreId)
                    .having("COUNT(DISTINCT medicine_status_code) = {0}", request.getMedicineStatusList().size()))
                    .stream()
                    .map(String::valueOf)
                    .toList();
            if (CollectionUtils.isEmpty(idListByMedicineStatus)) {
                return new PageResult<>();
            }
        }

        List<String> finalIdList = null;
        if (!CollectionUtils.isEmpty(idListByProvince) && !CollectionUtils.isEmpty(idListByMedicineStatus)) {
            finalIdList = idListByProvince.stream()
                    .filter(idListByMedicineStatus::contains)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(finalIdList)) {
                return new PageResult<>();
            }
        } else if (!CollectionUtils.isEmpty(idListByProvince)) {
            finalIdList = idListByProvince;
        } else if (!CollectionUtils.isEmpty(idListByMedicineStatus)) {
            finalIdList = idListByMedicineStatus;
        }

        LambdaQueryWrapper<Drugstore> queryWrapper = new LambdaQueryWrapper<Drugstore>()
                .like(StringUtil.isNotBlank(request.getName()), Drugstore::getName, request.getName())
                .in(!CollectionUtils.isEmpty(finalIdList), Drugstore::getId, finalIdList)
                .orderByDesc(Drugstore::getCreateTime);

        Page<Drugstore> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }

        return new PageResult<>(page.getRecords(), page.getTotal());
    }
}
