package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.SaveUpdateDrugstoreRequestDto;
import com.aihoo.domain.hospital.dto.SearchDrugstoreRequestDto;
import com.aihoo.domain.hospital.entity.Drugstore;
import com.aihoo.domain.hospital.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.hospital.entity.DrugstoreProvinceRel;
import com.aihoo.domain.hospital.mapper.DrugstoreMapper;
import com.aihoo.domain.hospital.service.DrugstoreMedicineStatusRelService;
import com.aihoo.domain.hospital.service.DrugstoreProvinceRelService;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public PageResult<Drugstore> getPage(PageParam<Drugstore> pageParam, String name, String provincesCode,
                                         List<Integer> medicineStatusList) {
        SearchDrugstoreRequestDto request = new SearchDrugstoreRequestDto();
        request.setName(name);
        request.setProvincesCode(provincesCode);
        request.setMedicineStatusList(medicineStatusList);
        return getPage(pageParam, request);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean create(SaveUpdateDrugstoreRequestDto request) {
        Drugstore drugstore = new Drugstore();
        BeanUtils.copyProperties(request, drugstore);
        boolean saved = save(drugstore);

        if (!CollectionUtils.isEmpty(request.getMedicineStatusList())) {
            List<DrugstoreMedicineStatusRel> medicineStatusRelList = request.getMedicineStatusList().stream().map(code -> {
                DrugstoreMedicineStatusRel rel = new DrugstoreMedicineStatusRel();
                rel.setDrugstoreId(drugstore.getId());
                rel.setMedicineStatusCode(code);
                return rel;
            }).toList();
            medicineStatusRelService.saveBatch(medicineStatusRelList);
        }

        if (!CollectionUtils.isEmpty(request.getProvinceList())) {
            List<DrugstoreProvinceRel> provinceRelList = request.getProvinceList().stream().map(code -> {
                DrugstoreProvinceRel rel = new DrugstoreProvinceRel();
                rel.setDrugstoreId(drugstore.getId());
                rel.setProvinceCode(code);
                return rel;
            }).toList();
            provinceRelService.saveBatch(provinceRelList);
        }
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SaveUpdateDrugstoreRequestDto request) {
        Drugstore drugstore = new Drugstore();
        BeanUtils.copyProperties(request, drugstore);
        boolean updated = updateById(drugstore);

        medicineStatusRelService.remove(new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                .eq(DrugstoreMedicineStatusRel::getDrugstoreId, drugstore.getId()));
        if (!CollectionUtils.isEmpty(request.getMedicineStatusList())) {
            List<DrugstoreMedicineStatusRel> medicineStatusRelList = request.getMedicineStatusList().stream().map(code -> {
                DrugstoreMedicineStatusRel rel = new DrugstoreMedicineStatusRel();
                rel.setDrugstoreId(drugstore.getId());
                rel.setMedicineStatusCode(code);
                return rel;
            }).toList();
            medicineStatusRelService.saveBatch(medicineStatusRelList);
        }

        provinceRelService.remove(new LambdaQueryWrapper<DrugstoreProvinceRel>()
                .eq(DrugstoreProvinceRel::getDrugstoreId, drugstore.getId()));
        if (!CollectionUtils.isEmpty(request.getProvinceList())) {
            List<DrugstoreProvinceRel> provinceRelList = request.getProvinceList().stream().map(code -> {
                DrugstoreProvinceRel rel = new DrugstoreProvinceRel();
                rel.setDrugstoreId(drugstore.getId());
                rel.setProvinceCode(code);
                return rel;
            }).toList();
            provinceRelService.saveBatch(provinceRelList);
        }
        return updated;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(String id) {
        boolean removed = removeById(id);
        medicineStatusRelService.remove(new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                .eq(DrugstoreMedicineStatusRel::getDrugstoreId, id));
        provinceRelService.remove(new LambdaQueryWrapper<DrugstoreProvinceRel>()
                .eq(DrugstoreProvinceRel::getDrugstoreId, id));
        return removed;
    }

    @Override
    public boolean updateStatus(String id, String status) {
        Drugstore drugstore = new Drugstore();
        drugstore.setId(id);
        drugstore.setStatus(status);
        return updateById(drugstore);
    }
}
