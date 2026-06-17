package com.aihoo.domain.sys.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.dto.DictTypeItemDto;
import com.aihoo.domain.sys.entity.Banner;
import com.aihoo.domain.sys.entity.Dict;
import com.aihoo.domain.sys.mapper.BannerMapper;
import com.aihoo.domain.sys.service.BannerService;
import com.aihoo.domain.sys.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Banner service 实现。
 *
 * <p>findDoctorAll / getBannerDetails 中的 doctor 域跨域查询通过 doctor 域 DoctorUserService 完成（Rule H 暂授权）。
 * <p>findDiseaseAll / teams 涉及 mdt 域，mdt 域尚未在 DDD 独立成域，暂返回空列表，迁移时按需补。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    private final BannerMapper bannerMapper;
    private final DictService dictService;

    @Override
    public PageResult<Map<String, Object>> bannerList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }

        QueryWrapper<Banner> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            wrapper.eq("type", map.get("type"));
        }
        wrapper.orderByAsc("`index`").orderByDesc("create_time");
        IPage<Banner> iPage = bannerMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Banner> bannerList = iPage.getRecords();
        List<Map<String, Object>> newBannerList = new ArrayList<>();
        for (Banner banner : bannerList) {
            Map<String, Object> ban = new HashMap<>();
            ban.put("img", banner.getImg() == null ? "" : banner.getImg());
            ban.put("index", banner.getIndex() == null ? "" : banner.getIndex());
            ban.put("title", banner.getTitle() == null ? "" : banner.getTitle());
            ban.put("type", "");
            ban.put("typeName", "");
            ban.put("bannerType", "");
            ban.put("bannerTypeName", "");
            ban.put("videoUrl", "");
            String bannerType = banner.getBannerType();
            if (bannerType != null) {
                ban.put("bannerType", bannerType);
                if (bannerType.equals("IMAGE")) {
                    ban.put("bannerTypeName", "图片");
                } else if (bannerType.equals("VIDEO")) {
                    ban.put("bannerTypeName", "视频");
                    ban.put("videoUrl", banner.getVideoUrl());
                }
            }
            String type = banner.getType();
            if (!StringUtils.isEmpty(type)) {
                ban.put("type", type);
                try {
                    Dict dict = dictService.lambdaQuery()
                            .eq(Dict::getType, "BRAND_TYPE")
                            .eq(Dict::getCode, type)
                            .list().stream().findFirst().orElse(null);
                    if (dict != null) {
                        ban.put("typeName", dict.getName());
                    }
                } catch (Exception ignore) {
                }
            }
            ban.put("content", banner.getContent() == null ? "" : banner.getContent());
            ban.put("id", banner.getId());
            newBannerList.add(ban);
        }
        return new PageResult<>(newBannerList, iPage.getTotal());
    }

    @Override
    public Boolean deleteBanner(String id) {
        Banner banner = new Banner();
        banner.setId(id);
        banner.setIsDelete("1");
        return bannerMapper.updateById(banner) > 0;
    }

    @Override
    public List<DictTypeItemDto> getDoctorType(String type) {
        return dictService.getDoctorType(type);
    }

    @Override
    public Map<String, Object> getBannerDetails(String id) {
        Banner banner = this.bannerMapper.selectById(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("bannerType", "");
        resp.put("bannerTypeName", "");
        resp.put("videoUrl", "");
        String bannerType = banner.getBannerType();
        if (bannerType != null) {
            resp.put("bannerType", bannerType);
            if (bannerType.equals("IMAGE")) {
                resp.put("bannerTypeName", "图片");
            } else if (bannerType.equals("VIDEO")) {
                resp.put("bannerTypeName", "视频");
                resp.put("videoUrl", banner.getVideoUrl());
            }
        }
        resp.put("id", banner.getId());
        resp.put("type", banner.getType());
        resp.put("otherId", banner.getOtherId());
        resp.put("title", banner.getTitle());
        resp.put("img", banner.getImg());
        resp.put("index", banner.getIndex());
        resp.put("name", "");
        resp.put("content", banner.getContent() == null ? "" : banner.getContent());

        if (!StringUtils.isEmpty(banner.getType())) {
            try {
                Dict dict = dictService.lambdaQuery()
                        .eq(Dict::getType, "BRAND_TYPE")
                        .eq(Dict::getCode, banner.getType())
                        .list().stream().findFirst().orElse(null);
                if (dict != null) {
                    resp.put("typeName", dict.getName());
                } else {
                    resp.put("typeName", "");
                }
            } catch (Exception e) {
                resp.put("typeName", "");
            }
        }
        return resp;
    }

    @Override
    public boolean addBanner(Map<String, Object> map) {
        Banner banner = new Banner();
        banner.setCreateUserId(com.aihoo.util.SecurityUtils.getLoginUserId());
        String type = map.get("type").toString();
        banner.setType(type);
        banner.setImg(map.get("img").toString());
        banner.setIndex(map.get("index").toString().trim());
        banner.setTitle(map.get("title") == null ? "" : map.get("title").toString());
        if (type.equals("DOCKER") || type.equals("DISEASE") || type.equals("MDTTEAM") || type.equals("MDTDOCTOR")) {
            banner.setOtherId(map.get("otherId").toString());
        } else if (type.equals("TEXTAREA")) {
            banner.setContent(map.get("content").toString());
        }
        String bannerType = map.get("bannerType").toString();
        banner.setBannerType(bannerType);
        if ("VIDEO".equals(bannerType)) {
            banner.setVideoUrl(map.get("videoUrl").toString());
        }
        String id = (map.get("id") == null ? null : map.get("id").toString());
        int a;
        if (id != null) {
            banner.setId(id);
            a = bannerMapper.updateById(banner);
            if (a <= 0) {
                a = bannerMapper.insert(banner);
            }
        } else {
            a = bannerMapper.insert(banner);
        }
        return a > 0;
    }

    @Override
    public List<Map<String, Object>> findDoctorAll() {
        // 跨域查询：doctor 域 DoctorUserService 通过 list() 获取（IService 已自带，调用方需保证 doctor 域已注册）
        // 当前不在 sys 域直接调用 doctor mapper，约定由 api 层或 controller 做跨域聚合（BannerController 改为不做此调用）
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> findDiseaseAll() {
        // mdt 域未独立成域，暂返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> teams(Map<String, Object> param) {
        // mdt team 未独立成域，暂返回空列表
        return new ArrayList<>();
    }
}
