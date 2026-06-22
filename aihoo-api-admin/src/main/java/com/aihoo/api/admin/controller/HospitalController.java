package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.vo.HospitalPageVo;
import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.entity.Hospital;
import com.aihoo.domain.drug.service.HospitalService;
import com.aihoo.exception.BizException;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 医院管理
 */
@RestController
@RequestMapping("/api/v1/hospital")
public class HospitalController extends BaseController {

    @Resource
    private HospitalService hospitalService;

    /**
     * 医院管理多条件分页查询
     */
    @PostMapping("/list")
    public PageResult<HospitalPageVo> list(@RequestBody Map<String, Object> map) {
        try {
            PageResult<Hospital> page = hospitalService.list(map);
            List<HospitalPageVo> voList = new ArrayList<>();
            if (page != null && !CollectionUtils.isEmpty(page.getData())) {
                for (Hospital hospital : page.getData()) {
                    HospitalPageVo vo = new HospitalPageVo();
                    BeanUtils.copyProperties(hospital, vo);
                    voList.add(vo);
                }
            }
            return new PageResult<>(voList, page == null ? 0L : page.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("医院分页查询出错");
        }
    }

    /**
     * 根据医院id 查询医院详情
     */
    @PostMapping("/controlHospital")
    public JsonResult controlHospital(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("id") || "".equals(map.get("id"))) {
                return error("未携带医院id");
            }
            JSONObject json = this.hospitalService.controlHospital(map.get("id").toString());
            return JsonResult.ok().put("data", json);
        } catch (Exception e) {
            if (e instanceof BizException) {
                return error(e.getMessage());
            }
            e.printStackTrace();
            return error("医院详情查询出错");
        }
    }

    /**
     * 查询所有科室对应关系
     */
    @PostMapping("/departmentRelation")
    public JsonResult departmentRelation() {
        try {
            JSONArray resp = this.hospitalService.departmentRelation();
            return JsonResult.ok().put("data", resp);
        } catch (Exception e) {
            e.printStackTrace();
            return error("查询所有科室对应关系出错");
        }
    }

    /**
     * 根据医院id 更新医院详情
     */
    @PostMapping("/hospitalUpdate")
    public JsonResult hospitalUpdate(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            JSONObject res = this.hospitalService.hospitalUpdate(map, request);
            if (res.isEmpty()) {
                return ok("更新成功");
            }
            JsonResult fail = error(res.getString("msg"));
            Object data = res.get("data");
            if (data != null) {
                fail.put("date", data);
            }
            return fail;
        } catch (Exception e) {
            e.printStackTrace();
            return error("更新医院详情出错");
        }
    }

    /**
     * 新增医院
     */
    @PostMapping("/hospitalInsert")
    public JsonResult hospitalInsert(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            JSONObject res = this.hospitalService.hospitalInsert(map, request);
            return res.isEmpty() ? ok("请求成功") : error(res.getString("msg"));
        } catch (Exception e) {
            e.printStackTrace();
            return error("新增医院出错");
        }
    }

    /**
     * 查询所有省市区联动信息
     */
    @PostMapping("/provincesRelation")
    public JsonResult provincesRelation() {
        try {
            JSONArray res = this.hospitalService.provincesRelation();
            return JsonResult.ok().put("data", res);
        } catch (Exception e) {
            e.printStackTrace();
            return error("省市区联动查询失败");
        }
    }

    /**
     * 根据id 更新医院状态
     */
    @PostMapping("/enableDisable")
    public JsonResult enableDisable(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            if (null == map.get("id") || "".equals(map.get("id").toString())) {
                return error("未携带医院id");
            }
            if (null == map.get("status") || "".equals(map.get("status").toString())) {
                return error("status不明确（未携带状态）");
            }
            Boolean boo = this.hospitalService.enableDisable(map, request);
            return boo != null && boo ? JsonResult.ok() : error("不存在的id" + map.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return error("请求出错");
        }
    }

    /**
     * 获取本系统已存在的医院
     */
    @PostMapping("/existHospitalAll")
    public JsonResult getExistHospitalAll() {
        List<Hospital> list = this.hospitalService.list();
        JSONArray jsonArray = new JSONArray();
        if (!CollectionUtils.isEmpty(list)) {
            for (Hospital s : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", s.getId());
                jsonObject.put("hosName", s.getHosName());
                jsonArray.add(jsonObject);
            }
        }
        return JsonResult.ok().put("data", jsonArray);
    }

    /**
     * 根据主键删除医院
     */
    @PostMapping("/delete")
    public JsonResult delete(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            if (null == map.get("id") || "".equals(map.get("id").toString())) {
                return error("未携带医院id");
            }
            Boolean boo = this.hospitalService.updateDel(map, request);
            return boo != null && boo ? JsonResult.ok() : error("不存在的id" + map.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return error("请求出错");
        }
    }
}
