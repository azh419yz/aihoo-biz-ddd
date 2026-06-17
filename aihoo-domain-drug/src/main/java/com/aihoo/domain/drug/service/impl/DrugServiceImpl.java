package com.aihoo.domain.drug.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aihoo.common.JsonResult;
import com.aihoo.domain.drug.entity.Drug;
import com.aihoo.domain.drug.mapper.DrugMapper;
import com.aihoo.domain.drug.service.DrugService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 药品 服务实现（迁自 patient-api 的 DrugServiceImpl）。
 *
 * @author carl
 * @since 2020-09-27
 */
@Service
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {
    private final DrugMapper drugMapper;

    public DrugServiceImpl(DrugMapper drugMapper) {
        this.drugMapper = drugMapper;
    }

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
}
