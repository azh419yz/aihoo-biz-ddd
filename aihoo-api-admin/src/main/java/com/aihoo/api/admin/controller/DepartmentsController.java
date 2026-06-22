package com.aihoo.api.admin.controller;

import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.entity.Department;
import com.aihoo.domain.drug.service.DepartmentService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 科室管理
 */
@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentsController extends BaseController {

    @Resource
    private DepartmentService departmentsService;

    /**
     * 一级科室对应的名称和编码
     */
    @PostMapping("/level/codeAndName")
    public JsonResult levelCodeAndName() {
        try {
            List<Department> departments = this.departmentsService.list(new QueryWrapper<Department>().eq("level", 1));
            if (CollectionUtils.isEmpty(departments)) {
                return error("未查询到一级科室数据");
            }
            JSONArray jsonArray = new JSONArray();
            for (Department s : departments) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", s.getCode());
                jsonObject.put("name", s.getName());
                jsonArray.add(jsonObject);
            }
            return ok("请求成功").put("data", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return error("科室列表查询出错");
        }
    }

    /**
     * 查询
     */
    @PostMapping("/list")
    public PageResult<Department> list(@RequestBody Map<String, Object> map) {
        try {
            return this.departmentsService.departmentsList(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("科室列表查询出错");
        }
    }

    /**
     * 科室新增
     */
    @PostMapping("/add")
    public JsonResult add(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("name") || "".equals(map.get("name"))) {
                return error("科室名称必填");
            }
            if (null == map.get("code") || "".equals(map.get("code"))) {
                return error("科室编码必填");
            }
            JsonResult res = this.departmentsService.add(map);
            Object msg = res.get("msg");
            return msg == null || msg.toString().isEmpty() ? ok("新增成功") : error(msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return error("新增出错");
        }
    }

    /**
     * 启用禁用
     */
    @PostMapping("/departmentEnableDisable")
    public JsonResult departmentEnableDisable(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("id") || "".equals(map.get("id"))) {
                return error("科室id必填");
            }
            if (null == map.get("status") || "".equals(map.get("status"))) {
                return error("科室状态必填");
            }
            this.departmentsService.departmentEnableDisable(map);
            return ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return error("更新出错");
        }
    }

    /**
     * 更新科室
     */
    @PostMapping("/update")
    public JsonResult departmentUpdate(@RequestBody Map<String, Object> map) {
        try {
            JsonResult res = this.departmentsService.departmentUpdate(map);
            Object msg = res.get("msg");
            return msg == null || msg.toString().isEmpty() ? ok("更新成功") : error(msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return error("更新科室失败！");
        }
    }

    /**
     * 查看下级科室
     */
    @PostMapping("/subordinate")
    public JsonResult departmentSubordinate(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("code") || "".equals(map.get("code"))) {
                return error("当前科室的code必填");
            }
            List<Department> list = this.departmentsService.list(new QueryWrapper<Department>().eq("parent_code", map.get("code")));
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return error("查询出错");
        }
    }

    /**
     * 查看上级科室
     */
    @PostMapping("/superior")
    public JsonResult departmentSuperior(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("parentCode") || "".equals(map.get("parentCode"))) {
                return error("当前科室的父级code必填");
            }
            List<Department> list = this.departmentsService.list(new QueryWrapper<Department>().eq("code", map.get("parentCode")).eq("status", 1));
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return error("查询出错");
        }
    }
}
