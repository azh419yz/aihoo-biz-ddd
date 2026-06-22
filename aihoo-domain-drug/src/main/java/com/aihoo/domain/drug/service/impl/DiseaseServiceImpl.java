package com.aihoo.domain.drug.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.entity.Disease;
import com.aihoo.domain.drug.mapper.DiseaseMapper;
import com.aihoo.domain.drug.service.DiseaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 疾病 服务实现（d_disease）。
 *
 * <p>迁自 aihoo-biz-service/aihoo-admin 的 DiseaseServiceImpl。
 */
@Service
public class DiseaseServiceImpl extends ServiceImpl<DiseaseMapper, Disease> implements DiseaseService {

    @Override
    public PageResult<Disease> diseaseList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<Disease> wrapper = new QueryWrapper<>();
        if (null != map.get("code") && !"".equals(map.get("code"))) {
            wrapper.eq("code", map.get("code"));
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like("name", map.get("name"));
        }
        IPage<Disease> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public boolean addDisease(Map<String, Object> map) {
        Disease disease = new Disease();
        disease.setName(map.get("name").toString().trim());
        disease.setCode(map.get("code").toString().trim());
        int insert = baseMapper.insert(disease);
        return insert > 0;
    }

    @Override
    public boolean updateDisease(Map<String, Object> map) {
        Disease disease = new Disease();
        disease.setId(map.get("id").toString());
        disease.setIsDelete(map.get("isDelete").toString());
        int update = baseMapper.updateById(disease);
        return update > 0;
    }
}
