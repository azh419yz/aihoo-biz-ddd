package com.aihoo.domain.drug.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.dto.SaveUpdateDrugRequestDto;
import com.aihoo.domain.drug.dto.SearchDrugRequestDto;
import com.aihoo.domain.drug.entity.Drug;
import com.aihoo.domain.drug.entity.Drugstore;
import com.aihoo.domain.drug.mapper.DrugMapper;
import com.aihoo.domain.drug.service.DrugService;
import com.aihoo.domain.drug.service.DrugstoreService;
import com.aihoo.exception.BizException;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 药品 服务实现。
 *
 * <p>patient/doctor 阶段方法保留；admin 阶段新增 CRUD + drugBulkExport 桩。
 */
@Service
@RequiredArgsConstructor
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {

    private final DrugMapper drugMapper;
    private final DrugstoreService drugstoreService;

    @Override
    public JsonResult drugPriceList(Map<String, String> map) {
        long page = 1;
        long limit = 10;
        if (StrUtil.isNotBlank(map.get("page"))) {
            page = Long.parseLong(map.get("page"));
        }
        if (StrUtil.isNotBlank(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit"));
        }
        IPage<Drug> iPage = new Page<>(page, limit);
        IPage<Drug> drugIPage = drugMapper.selectPage(iPage, null);
        List<Drug> records = drugIPage.getRecords();
        return JsonResult.ok().putData(records);
    }

    @Override
    public PageResult<Drug> getPage(PageParam<Drug> pageParam, SearchDrugRequestDto request) {
        LambdaQueryWrapper<Drug> queryWrapper = new LambdaQueryWrapper<Drug>()
                .eq(Drug::getDrugstoreId, request.getDrugstoreId())
                .eq(Drug::getStatus, "1")
                .like(StringUtil.isNotBlank(request.getName()), Drug::getName, request.getName())
                .likeRight(StringUtil.isNotBlank(request.getInitial()), Drug::getPinyinInitial,
                        request.getInitial().toUpperCase())
                .orderByAsc(Drug::getPrice);

        Page<Drug> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public boolean create(SaveUpdateDrugRequestDto request) {
        String drugstoreId = request.getDrugstoreId();
        Drugstore drugstore = drugstoreService.getById(drugstoreId);
        if (drugstore == null) {
            return false;
        }
        Drug drug = new Drug();
        BeanUtils.copyProperties(request, drug);
        return save(drug);
    }

    @Override
    public boolean update(SaveUpdateDrugRequestDto request) {
        Drug drug = new Drug();
        BeanUtils.copyProperties(request, drug);
        return updateById(drug);
    }

    @Override
    public boolean delete(String id) {
        return removeById(id);
    }

    @Override
    public boolean updateStatus(String id, String status) {
        Drug drug = new Drug();
        drug.setId(id);
        drug.setStatus(status);
        return updateById(drug);
    }

    @Override
    public void drugBulkExport(String name, String manufacturers, HttpServletRequest request, HttpServletResponse response) {
        throw new BizException("药品批量导出暂未实现");
    }
}
