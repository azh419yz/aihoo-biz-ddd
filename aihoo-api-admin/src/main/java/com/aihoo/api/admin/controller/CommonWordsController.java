package com.aihoo.api.admin.controller;

import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.domain.sys.model.vo.TBaseVo;
import com.aihoo.domain.sys.service.TBaseService;
import com.aihoo.util.StringHandler;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 常用语配置
 */
@RestController
@RequestMapping("/api/v1/commonWords")
public class CommonWordsController extends BaseController {


    @Resource
    private TBaseService tBaseService;

    /**
     * 常用语列表
     *
     * @param
     * @return
     */
    @PostMapping("/page")
    public JsonResult page() {
        try {
            List<TBaseVo> tBaseVos = tBaseService.pageList();
            return ok().put("data", tBaseVos);
        } catch (Exception e) {
            e.printStackTrace();
            return error("常用语列表查询失败");
        }
    }


    /**
     * 新增常用语
     *
     * @param map 入参
     * @return {}
     */
    @PostMapping("/save")
    public JsonResult save(@RequestBody Map<String, Object> map) {
        try {
            if (StringHandler.isEmpty(String.valueOf(map.get("content")))
                    || StringHandler.isEmpty(String.valueOf(map.get("index")))) {
                return error("参数不能为空");
            }
            boolean save = tBaseService.addCommonWords(map);
            if (!save) {
                return error("新增常用语失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("新增常用语失败");
        }
        return JsonResult.ok();
    }


    /**
     * 更新常用语
     *
     * @param map 入参
     * @return {}
     */
    @PostMapping("/update")
    public JsonResult update(@RequestBody Map<String, Object> map) {
        try {
            if (StringHandler.isEmpty(String.valueOf(map.get("id")))) {
                return error("id不能为空");
            }
            if (StringHandler.isEmpty(String.valueOf(map.get("content")))
                    && StringHandler.isEmpty(String.valueOf(map.get("index")))) {
                return error("参数不能为空");
            }
            boolean update = tBaseService.updateCommonWords(map);
            if (!update) {
                return error("更新常用语失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("更新常用语失败");
        }
        return JsonResult.ok();
    }


    /**
     * 根据主键删除常用语
     *
     * @param map
     * @return JsonResult
     */
    @PostMapping("/delete")
    public JsonResult delete(@RequestBody Map<String, Object> map) {
        try {
            if (StringHandler.isEmpty(String.valueOf(map.get("id")))) {
                return error("id不能为空");
            }
            boolean del = tBaseService.removeById(String.valueOf(map.get("id")));
            if (!del) {
                return error("删除常用语失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("请求出错");
        }
        return JsonResult.ok();
    }

}
