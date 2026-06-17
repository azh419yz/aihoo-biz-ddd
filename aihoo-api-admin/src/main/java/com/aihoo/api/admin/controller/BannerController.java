package com.aihoo.api.admin.controller;

/**
 * Created by xieyc on 2020/8/13.
 */

import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.model.entity.Banner;
import com.aihoo.domain.sys.service.BannerService;
import com.aihoo.enums.BrandTypeEnum;
import com.aihoo.properties.*;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * banner管理
 **/
@RestController
@RequestMapping("/api/v1/banner")
public class BannerController extends BaseController {

    @Resource
    private BannerService bannerService;

    /**
     * 根据bannerId查询banner详情
     *
     * @param map 入参
     * @return {}
     */
    @PostMapping("/bannerDetails")
    public JsonResult getBannerDetails(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null) {
            return error("请传id");
        }
        try {
            JSONObject resp = bannerService.getBannerDetails(map.get("id").toString());
            return JsonResult.ok().put("data", resp);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }


    /**
     * 测试配置参数注入，无用可删除
     */
    @Resource
    private AlipayProperties alipayProperties;
    @Resource
    private MeiqingProperties meiqingProperties;
    @Resource
    private TencentProperties tencentProperties;
    @Resource
    private TestProperties testProperties;
    @Resource
    private WechatProperties wechatProperties;
    @Resource
    private CaProperties caProperties;

    @GetMapping("/test")
    public void getTest() {
        System.out.println(alipayProperties.getAppId());
        System.out.println(meiqingProperties.getAccept());
        System.out.println(tencentProperties.getPrivstr());
        System.out.println(testProperties.getCode());
        System.out.println(wechatProperties.getAppId());
        System.out.println(caProperties.getAppId());
    }

    /**
     * 查询所有的医生姓名和对应id
     *
     * @return {}
     */
    @PostMapping("/findDoctorAll")
    public JsonResult findDoctorAll() {
        try {
            JSONArray jsonArray = bannerService.findDoctorAll();
            return JsonResult.ok().put("data", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return error("查询出错");
        }
    }

    /**
     * 查询所有的疾病和对应的id
     *
     * @return {}
     */
    @PostMapping("/findDiseaseAll")
    public JsonResult findDiseaseAll() {
        try {
            JSONArray jsonArray = bannerService.findDiseaseAll();
            return JsonResult.ok().put("data", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return error("查询出错");
        }
    }

    /**
     * 查询各种 类型对应的 类型名称  以及对应类型的code 和 name
     * *
     *
     * @return {}
     */
    @PostMapping("/getDoctorByType")
    public JsonResult getDoctorType(@RequestBody Map<String, Object> map) {
        if (null == map.get("type") || "".equals(map.get("type"))) {
            return error("请填写type");
        }
        try {
            JSONArray jsonArray = bannerService.getDoctorType(map.get("type").toString());
            return JsonResult.ok().put("data", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * banner列表
     *
     * @return {}
     */
    @PostMapping("/list")
    public PageResult list(@RequestBody Map<String, Object> map) {
        try {
            PageResult data = bannerService.bannerList(map);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<Banner>("banner列表查询出错");
        }
    }

    /**
     * banner标记删除
     *
     * @return {}
     */
    @PostMapping("/delete")
    public JsonResult delete(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("id")) {
                return error("参数bannerId不能为空");
            }
            Boolean boo = bannerService.deleteBanner(map.get("id").toString());
            return boo ? JsonResult.ok() : error("不存在的id" + map.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return error("删除失败");
        }
    }

    /**
     * 新增
     *
     * @param map 入参
     * @return {}
     */
    @PostMapping("/addOrUpdate")
    public JsonResult add(@RequestBody Map<String, Object> map) {
        if (null == map.get("bannerType") || "".equals(map.get("bannerType").toString())) {
            return error("banner为视频类型时，视频不能为空");
        }
        String bannerType = map.get("bannerType").toString();
        if (bannerType.equals("VIDEO") && (null == map.get("videoUrl") || "".equals(map.get("videoUrl").toString()))) {
            return error("请选择图片或视频类型");
        }
        if (null == map.get("img") || "".equals(map.get("img"))) {
            return error("请上传banner图片");
        } else if (null == map.get("index")) {
            return error("请填写序列");
        } else if (null == map.get("type")) {
            return error("请选择banner类型");
        } else if (map.get("type").equals(BrandTypeEnum.DOCKER.getCode()) && StringUtils.isEmpty(map.get("otherId"))) {
            return error("banner为医生类型时，选择医生不能为空");
        } else if (map.get("type").equals(BrandTypeEnum.DISEASE.getCode()) && StringUtils.isEmpty(map.get("otherId"))) {
            return error("banner为疾病类型时，选择疾病不能为空");
        } else if (map.get("type").equals(BrandTypeEnum.TEXTAREA.getCode()) && (null == map.get("content") || null == map.get("title"))) {
            return error("banner为富文本类型时，标题或者内容不能为空");
        } else if (map.get("type").equals(BrandTypeEnum.MDTDOCTOR.getCode()) && (null == map.get("content") || null == map.get("title"))) {
            return error("banner为会诊医生类型时，标题或者内容不能为空");
        } else if (map.get("type").equals(BrandTypeEnum.MDTTEAM.getCode()) && (null == map.get("content") || null == map.get("title"))) {
            return error("banner为会诊团队类型时，标题或者内容不能为空");
        }
        if (BrandTypeEnum.NONE.getCode().equals(map.get("type").toString()) && !StringUtils.isEmpty(map.get("isTeam"))) {
            String isTeam = map.get("isTeam").toString();
            if (isTeam.equals("0")) {
                map.put("type", BrandTypeEnum.MDTDOCTOR.getCode());
            } else if (isTeam.equals("1")) {
                map.put("type", BrandTypeEnum.MDTTEAM.getCode());
            }
        }
        try {
            boolean b = bannerService.addBanner(map);
            return b ? JsonResult.ok() : error("新增失败");
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作错误");
        }

    }
}
