package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.dto.AreaDto;
import com.aihoo.domain.sys.entity.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 地区表 服务接口（合并旧 AreaService（admin/doctor）与 DAreaService（patient））。
 *
 * <p>旧代码中两组方法名不同但逻辑一致，统一保留：
 * <ul>
 *   <li>provincesRelation() = l3List() —— 省市区三级联动</li>
 *   <li>doctorProCityList() = l2List() —— 省市二级联动</li>
 * </ul>
 *
 * @author mcp
 * @since 2020-08-10
 */
public interface AreaService extends IService<Area> {

    String getAreaByCode(String code);

    /** 省市区三级联动（DAreaService.provincesRelation / AreaService.l3List） */
    List<AreaDto> provincesRelation();

    /** 省市二级联动（DAreaService.doctorProCityList / AreaService.l2List） */
    List<AreaDto> doctorProCityList();

    /** 省市区三级联动（admin/doctor 老方法名，逻辑同 provincesRelation） */
    List<AreaDto> l3List();

    /** 省市二级联动（admin/doctor 老方法名，逻辑同 doctorProCityList） */
    List<AreaDto> l2List();

    List<Area> provincesList();

    List<Area> cityList(String parentAreaCode);

    List<Area> districtList(String parentAreaCode);
}