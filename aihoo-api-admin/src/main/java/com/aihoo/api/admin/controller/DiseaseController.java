package com.aihoo.api.admin.controller;

import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.entity.Disease;
import com.aihoo.domain.drug.service.DiseaseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 疾病管理
 */
@RestController
@RequestMapping("/api/v1/disease")
public class DiseaseController extends BaseController {

    @Resource
    private DiseaseService diseaseService;

    /**
     * 疾病列表
     */
    @PostMapping("/list")
    public PageResult<Disease> list(@RequestBody Map<String, Object> map) {
        try {
            return this.diseaseService.diseaseList(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("疾病列表查询出错");
        }
    }

    /**
     * 疾病新增
     */
    @PostMapping("/add")
    public JsonResult add(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("name") || "".equals(map.get("name"))) {
                return error("疾病名称必填");
            }
            if (null == map.get("code") || "".equals(map.get("code"))) {
                return error("疾病编码必填");
            }
            boolean save = this.diseaseService.addDisease(map);
            return save ? ok("新增成功") : error("新增出错");
        } catch (Exception e) {
            e.printStackTrace();
            return error("新增出错");
        }
    }

    /**
     * 启用禁用
     */
    @PostMapping("/diseaseEnableDisable")
    public JsonResult diseaseEnableDisable(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("id") || "".equals(map.get("id"))) {
                return error("疾病id必填");
            }
            if (null == map.get("isDelete") || "".equals(map.get("isDelete"))) {
                return error("疾病状态必填");
            }
            boolean b = this.diseaseService.updateDisease(map);
            return b ? ok("更新成功") : error("不存在的id" + map.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return error("更新出错");
        }
    }
}
