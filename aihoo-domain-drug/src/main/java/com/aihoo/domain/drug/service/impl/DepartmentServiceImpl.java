package com.aihoo.domain.drug.service.impl;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.entity.Department;
import com.aihoo.domain.drug.mapper.DepartmentMapper;
import com.aihoo.domain.drug.service.DepartmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 基础科室 服务实现（t_department）。
 *
 * <p>迁自 aihoo-biz-service/aihoo-admin 的 DepartmentServiceImpl。
 * <p>DicDepartment 医保上报逻辑已跳过（桩实现），保留主流程。
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public String findDepartmentNameByCode(String code) {
        List<Department> list = baseMapper.selectList(new QueryWrapper<Department>().eq("code", code));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0).getName();
    }

    @Override
    public PageResult<Department> departmentsList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }

        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        if (null != map.get("code") && !"".equals(map.get("code"))) {
            wrapper.eq("code", map.get("code"));
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like("name", map.get("name"));
        }
        IPage<Department> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public JsonResult add(Map<String, Object> map) {
        JsonResult res = JsonResult.ok();
        Department department = new Department();
        String level;
        if (null != map.get("iconImg") && !"".equals(map.get("iconImg"))) {
            department.setIconImg(map.get("iconImg").toString().trim());
        }
        if (null != map.get("index") && !"".equals(map.get("index"))) {
            department.setIndex(map.get("index").toString().trim());
        }
        if (null != map.get("parentCode") && !"".equals(map.get("parentCode"))) {
            department.setParentCode(map.get("parentCode").toString());
            level = "2";
            department.setLevel(level);
        } else {
            level = "1";
            department.setLevel(level);
        }
        List<Department> list = baseMapper.selectList(new QueryWrapper<Department>().eq("code", map.get("code")));
        if (!CollectionUtils.isEmpty(list)) {
            res = JsonResult.error("该科室的code已经存在");
            return res;
        }
        department.setName(map.get("name").toString().trim());
        department.setCode(map.get("code").toString().trim());
        baseMapper.insert(department);
        // 桩：跳过 DicDepartment 医保上报（原 admin 写 tb_dic_department 表）
        return JsonResult.ok();
    }

    @Override
    public JsonResult departmentUpdate(Map<String, Object> map) {
        JsonResult res = JsonResult.ok();
        if (null == map.get("id") || "".equals(map.get("id"))) {
            return JsonResult.error("科室id必填");
        }
        Department department = new Department();
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            department.setName(map.get("name").toString().trim());
        }
        if (null != map.get("iconImg") && !"".equals(map.get("iconImg"))) {
            department.setIconImg(map.get("iconImg").toString());
        }
        if (null != map.get("index") && !"".equals(map.get("index"))) {
            department.setIndex(map.get("index").toString().trim());
        }
        if (null != map.get("parentCode") && !"".equals(map.get("parentCode"))) {
            department.setParentCode(map.get("parentCode").toString().trim());
        }
        department.setId(map.get("id").toString());
        baseMapper.updateById(department);
        return res;
    }

    @Override
    public void departmentEnableDisable(Map<String, Object> map) {
        Department department = new Department();
        department.setId(map.get("id").toString());
        department.setStatus(map.get("status").toString());
        baseMapper.updateById(department);
        // 桩：跳过 DicDepartment 医保上报
    }
}
