package com.aihoo.domain.sys.service;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.dto.BannerDto;
import com.aihoo.domain.sys.dto.DictTypeItemDto;
import com.aihoo.domain.sys.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface BannerService extends IService<Banner> {

    PageResult<Map<String, Object>> bannerList(Map<String, Object> map);

    Boolean deleteBanner(String id);

    List<DictTypeItemDto> getDoctorType(String type);

    Map<String, Object> getBannerDetails(String id);

    boolean addBanner(Map<String, Object> map);

    List<Map<String, Object>> findDoctorAll();

    List<Map<String, Object>> findDiseaseAll();

    List<Map<String, Object>> teams(Map<String, Object> param);
}
