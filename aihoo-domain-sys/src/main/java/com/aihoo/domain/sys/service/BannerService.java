package com.aihoo.domain.sys.service;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.dto.BannerDto;
import com.aihoo.domain.sys.dto.DictTypeItemDto;
import com.aihoo.domain.sys.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * Banner service。
 *
 * <p>跨域依赖（Rule H 暂授权）：
 * <ul>
 *   <li>doctor 域 DoctorUserService / DoctorUserMapper — findDoctorAll / getBannerDetails 中查询医生</li>
 *   <li>mdt 实体暂未在 DDD 中独立成域（仅有 order 域的 MdtOrder），findDiseaseAll / teams 方法暂返回空列表，迁移时按需补 mdt 域</li>
 * </ul>
 */
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
