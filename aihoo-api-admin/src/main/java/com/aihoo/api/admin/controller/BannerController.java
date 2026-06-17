package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.vo.DictTypeItemVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.dto.DictTypeItemDto;
import com.aihoo.domain.sys.service.BannerService;
import com.aihoo.domain.sys.service.DictService;
import com.aihoo.properties.AlipayProperties;
import com.aihoo.properties.CaProperties;
import com.aihoo.properties.MeiqingProperties;
import com.aihoo.properties.TencentProperties;
import com.aihoo.properties.TestProperties;
import com.aihoo.properties.WechatProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Banner", description = "Banner管理")
@RestController
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;
    private final DictService dictService;

    private final AlipayProperties alipayProperties;
    private final MeiqingProperties meiqingProperties;
    private final TencentProperties tencentProperties;
    private final TestProperties testProperties;
    private final WechatProperties wechatProperties;
    private final CaProperties caProperties;

    @PostMapping("/bannerDetails")
    public BizResult<Map<String, Object>> getBannerDetails(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请传id");
        }
        Map<String, Object> data = bannerService.getBannerDetails(map.get("id").toString());
        return BizResult.success(data);
    }

    @GetMapping("/test")
    public BizResult<Void> getTest() {
        System.out.println(alipayProperties.getAppId());
        System.out.println(meiqingProperties.getAccept());
        System.out.println(tencentProperties.getPrivstr());
        System.out.println(testProperties.getCode());
        System.out.println(wechatProperties.getAppId());
        System.out.println(caProperties.getAppId());
        return BizResult.success();
    }

    @PostMapping("/findDoctorAll")
    public BizResult<List<Map<String, Object>>> findDoctorAll() {
        return BizResult.success(bannerService.findDoctorAll());
    }

    @PostMapping("/findDiseaseAll")
    public BizResult<List<Map<String, Object>>> findDiseaseAll() {
        return BizResult.success(bannerService.findDiseaseAll());
    }

    @PostMapping("/getDoctorByType")
    public BizResult<List<DictTypeItemVo>> getDoctorType(@RequestBody Map<String, Object> map) {
        if (map.get("type") == null || "".equals(map.get("type"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请填写type");
        }
        List<DictTypeItemDto> dtos = bannerService.getDoctorType(map.get("type").toString());
        return BizResult.success(dtos.stream().map(this::toVo).collect(Collectors.toList()));
    }

    @PostMapping("/list")
    public BizResult<PageResult<Map<String, Object>>> list(@RequestBody Map<String, Object> map) {
        return BizResult.success(bannerService.bannerList(map));
    }

    @PostMapping("/delete")
    public BizResult<Void> delete(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "参数bannerId不能为空");
        }
        Boolean ok = bannerService.deleteBanner(map.get("id").toString());
        return ok ? BizResult.success() : BizResult.fail(com.aihoo.common.BizResultCode.NOT_FOUND, "不存在的id" + map.get("id"));
    }

    @PostMapping("/addOrUpdate")
    public BizResult<Void> add(@RequestBody Map<String, Object> map) {
        if (map.get("bannerType") == null || "".equals(map.get("bannerType").toString())) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "banner为视频类型时，视频不能为空");
        }
        String bannerType = map.get("bannerType").toString();
        if (bannerType.equals("VIDEO") && (map.get("videoUrl") == null || "".equals(map.get("videoUrl").toString()))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请选择图片或视频类型");
        }
        if (map.get("img") == null || "".equals(map.get("img"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请上传banner图片");
        }
        if (map.get("index") == null) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请填写序列");
        }
        if (map.get("type") == null) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "请选择banner类型");
        }
        String type = map.get("type").toString();
        if (type.equals("DOCKER") && StringUtils.isEmpty(map.get("otherId"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "banner为医生类型时，选择医生不能为空");
        }
        if (type.equals("DISEASE") && StringUtils.isEmpty(map.get("otherId"))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "banner为疾病类型时，选择疾病不能为空");
        }
        if (type.equals("TEXTAREA") && (map.get("content") == null || map.get("title") == null)) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "banner为富文本类型时，标题或者内容不能为空");
        }
        if (type.equals("MDTDOCTOR") && (map.get("content") == null || map.get("title") == null)) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "banner为会诊医生类型时，标题或者内容不能为空");
        }
        if (type.equals("MDTTEAM") && (map.get("content") == null || map.get("title") == null)) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "banner为会诊团队类型时，标题或者内容不能为空");
        }
        if (type.equals("NONE") && !StringUtils.isEmpty(map.get("isTeam"))) {
            String isTeam = map.get("isTeam").toString();
            if (isTeam.equals("0")) {
                map.put("type", "MDTDOCTOR");
            } else if (isTeam.equals("1")) {
                map.put("type", "MDTTEAM");
            }
        }
        boolean ok = bannerService.addBanner(map);
        return ok ? BizResult.success() : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "新增失败");
    }

    @PostMapping("/teams")
    public BizResult<List<Map<String, Object>>> teams(@RequestBody Map<String, Object> map) {
        return BizResult.success(bannerService.teams(map));
    }

    private DictTypeItemVo toVo(DictTypeItemDto dto) {
        DictTypeItemVo vo = new DictTypeItemVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
