package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.entity.Department;
import com.aihoo.domain.hospital.entity.Hospital;
import com.aihoo.domain.hospital.entity.HospitalDepartment;
import com.aihoo.domain.hospital.mapper.DepartmentMapper;
import com.aihoo.domain.hospital.mapper.HospitalDepartmentMapper;
import com.aihoo.domain.hospital.mapper.HospitalMapper;
import com.aihoo.domain.hospital.service.DepartmentService;
import com.aihoo.domain.hospital.service.HospitalDepartmentService;
import com.aihoo.domain.hospital.service.HospitalService;
import com.aihoo.domain.hospital.util.LoginRecordUtil;
import com.aihoo.domain.sys.entity.Area;
import com.aihoo.domain.sys.service.AreaService;
import com.aihoo.domain.sys.service.DictService;
import com.aihoo.enums.DictTypeEnum;
import com.aihoo.util.DateUtil;
import com.aihoo.util.IdUtils;
import com.aihoo.util.SecurityUtils;
import com.aihoo.util.StringHandler;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {

    private final HospitalMapper hospitalMapper;
    private final HospitalDepartmentMapper hospitalDepartmentMapper;
    private final DepartmentMapper departmentMapper;
    private final AreaService areaService;
    private final DictService dictService;
    private final DepartmentService departmentService;
    private final HospitalDepartmentService hospitalDepartmentService;
    private final LoginRecordUtil loginRecordUtil;

    @Override
    public PageResult<Hospital> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<Hospital> wrapper = new QueryWrapper<>();
        if (null != map.get("hosName") && !"".equals(map.get("hosName").toString())) {
            wrapper.like("hos_name", map.get("hosName"));
        }
        if (null != map.get("hosGradeCode") && !"".equals(map.get("hosGradeCode").toString())) {
            wrapper.eq("hos_grade_code", map.get("hosGradeCode"));
        }
        if (null != map.get("hosCateCode") && !"".equals(map.get("hosCateCode").toString())) {
            wrapper.eq("hos_cate_code", map.get("hosCateCode"));
        }
        if (null != map.get("hosLevelCode") && !"".equals(map.get("hosLevelCode").toString())) {
            wrapper.eq("hos_level_code", map.get("hosLevelCode"));
        }
        wrapper.eq("is_delete", 0);
        if (null != map.get("hosAttCode") && !"".equals(map.get("hosAttCode").toString())) {
            wrapper.eq("hos_att_code", map.get("hosAttCode"));
        }
        wrapper.orderByDesc("create_time");
        IPage<Hospital> iPage = hospitalMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public JSONObject controlHospital(String id) {
        Hospital hospital = hospitalMapper.selectOne(new QueryWrapper<Hospital>().eq("id", id));
        if (null == hospital) {
            log.error("根据医院id未查询到医院 id为{}", id);
            throw new com.aihoo.exception.BizException("根据医院id未查询到医院");
        }
        JSONObject hospitalDetailsVo = JSONObject.parseObject(JSON.toJSON(hospital).toString());

        List<Department> departments = departmentMapper.selectList(new QueryWrapper<Department>().eq("status", 1));
        if (CollectionUtils.isEmpty(departments)) {
            log.error("状态为启用的科室不存在");
            throw new NullPointerException();
        }
        Map<String, List<Department>> levelMap = departments.stream().collect(Collectors.groupingBy(Department::getLevel));
        List<Department> firstLevel = levelMap.get("1");
        List<Department> secondLevel = levelMap.get("2");
        if (secondLevel == null || firstLevel == null) {
            throw new NullPointerException("科室 level 数据缺失");
        }
        Map<String, List<Department>> collect = secondLevel.stream().collect(Collectors.groupingBy(Department::getParentCode));
        JSONArray jsonArray = new JSONArray();
        List<HospitalDepartment> departmentList = hospitalDepartmentMapper.selectList(new QueryWrapper<HospitalDepartment>().eq("hospital_id", id));
        if (CollectionUtils.isEmpty(departmentList)) {
            log.error("根据医院的id未查询到医院详情 id为{}", id);
            throw new com.aihoo.exception.BizException("根据医院的id未查询到医院详情");
        }
        List<String> codeList = departmentList.stream().map(HospitalDepartment::getDepartCode).collect(Collectors.toList());
        for (Department department : firstLevel) {
            JSONArray secondJsonArray = new JSONArray();
            JSONObject resJson = new JSONObject();
            resJson.put("code", department.getCode());
            resJson.put("title", department.getName());
            if (codeList.contains(department.getCode())) {
                resJson.put("isSelect", "1");
                resJson.put("expand", true);
            } else {
                resJson.put("isSelect", "0");
            }
            List<Department> list = collect.get(department.getCode());
            if (!CollectionUtils.isEmpty(list)) {
                for (Department s : list) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", s.getCode());
                    jsonObject.put("title", s.getName());
                    if (codeList.contains(s.getCode())) {
                        jsonObject.put("isSelect", "1");
                        jsonObject.put("checked", true);
                    } else {
                        jsonObject.put("isSelect", "0");
                    }
                    secondJsonArray.add(jsonObject);
                }
                resJson.put("children", secondJsonArray);
            }
            jsonArray.add(resJson);
        }
        hospitalDetailsVo.put("departCodeArray", jsonArray);
        return hospitalDetailsVo;
    }

    @Override
    public JSONArray departmentRelation() {
        List<Department> departments = departmentMapper.selectList(new QueryWrapper<Department>().eq("status", 1));
        if (CollectionUtils.isEmpty(departments)) {
            log.error("状态为启用的科室不存在");
            throw new NullPointerException();
        }
        Map<String, List<Department>> map = departments.stream().collect(Collectors.groupingBy(Department::getLevel));
        List<Department> firstLevel = map.get("1");
        List<Department> secondLevel = map.get("2");
        if (secondLevel == null || firstLevel == null) {
            throw new NullPointerException("科室 level 数据缺失");
        }
        Map<String, List<Department>> collect = secondLevel.stream().collect(Collectors.groupingBy(Department::getParentCode));
        JSONArray jsonArray = new JSONArray();
        for (Department department : firstLevel) {
            JSONArray secondJsonArray = new JSONArray();
            JSONObject resJson = new JSONObject();
            resJson.put("code", department.getCode());
            resJson.put("title", department.getName());
            resJson.put("name", department.getName());
            List<Department> list = collect.get(department.getCode());
            if (!CollectionUtils.isEmpty(list)) {
                for (Department s : list) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", s.getCode());
                    jsonObject.put("title", s.getName());
                    jsonObject.put("name", s.getName());
                    secondJsonArray.add(jsonObject);
                }
                resJson.put("children", secondJsonArray);
                resJson.put("secondDepartment", secondJsonArray);
            }
            jsonArray.add(resJson);
        }
        return jsonArray;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject hospitalUpdate(Map<String, Object> map, HttpServletRequest request) {
        JSONObject res = new JSONObject();
        if (null == map.get("id") || "".equals(map.get("id"))) {
            res.put("msg", "请携带医院id");
            return res;
        }
        Hospital hospital = new Hospital();
        hospital.setId(map.get("id").toString().trim());
        if (null != map.get("hosName") && !"".equals(map.get("hosName"))) {
            Hospital byId = hospitalMapper.selectById(map.get("id").toString());
            if (byId.getHosName().equals(map.get("hosName"))) {
                hospital.setHosName(map.get("hosName").toString().trim());
            } else {
                List<Hospital> selectList = hospitalMapper.selectList(new QueryWrapper<Hospital>().eq("is_delete", 0));
                List<String> hospitalList = selectList.stream().map(Hospital::getHosName).collect(Collectors.toList());
                if (hospitalList.contains(map.get("hosName"))) {
                    res.put("msg", "医院名称已存在");
                    return res;
                } else {
                    hospital.setHosName(map.get("hosName").toString().trim());
                }
            }
        }
        if (null != map.get("hosMobile") && !"".equals(map.get("hosMobile"))) {
            hospital.setHosMobile(map.get("hosMobile").toString().trim());
        }
        if (null != map.get("hosGradeCode") && !"".equals(map.get("hosGradeCode"))) {
            String gradeName = this.dictService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_GRADE.getType(), map.get("hosGradeCode").toString());
            if (!StringHandler.isNotBlank(gradeName)) {
                res.put("msg", "未找到编码对应的医院等级");
                return res;
            }
            hospital.setHosGradeCode(map.get("hosGradeCode").toString().trim());
            hospital.setHosGradeName(gradeName);
        }
        if (null != map.get("hosLevelCode") && !"".equals(map.get("hosLevelCode"))) {
            String levelName = this.dictService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_LEVEL.getType(), map.get("hosLevelCode").toString());
            if (!StringHandler.isNotBlank(levelName)) {
                res.put("msg", "未找到编码对应的医院级别");
                return res;
            }
            hospital.setHosLevelCode(map.get("hosLevelCode").toString().trim());
            hospital.setHosLevelName(levelName);
        }
        if (null != map.get("hosCateCode") && !"".equals(map.get("hosCateCode"))) {
            String cateName = this.dictService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_CATE.getType(), map.get("hosCateCode").toString());
            if (!StringHandler.isNotBlank(cateName)) {
                res.put("msg", "未找到编码对应的医院类型");
                return res;
            }
            hospital.setHosCateCode(map.get("hosCateCode").toString().trim());
            hospital.setHosCateName(cateName);
        }
        if (null != map.get("hosAttCode") && !"".equals(map.get("hosAttCode"))) {
            String attName = this.dictService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_ATT.getType(), map.get("hosAttCode").toString());
            if (!StringHandler.isNotBlank(attName)) {
                res.put("msg", "未找到编码对应的医院属性");
                return res;
            }
            hospital.setHosAttCode(map.get("hosAttCode").toString().trim());
            hospital.setHosAttName(attName);
        }
        if (null != map.get("provinceCode") && !"".equals(map.get("provinceCode"))) {
            String provinceName = this.areaService.getAreaByCode(map.get("provinceCode").toString());
            if (!StringHandler.isNotBlank(provinceName)) {
                res.put("msg", "未找到编码对应的省");
                return res;
            }
            hospital.setProvinceCode(map.get("provinceCode").toString().trim());
            hospital.setProvince(provinceName);
        }
        if (null != map.get("cityCode") && !"".equals(map.get("cityCode"))) {
            String cityName = this.areaService.getAreaByCode(map.get("cityCode").toString());
            if (!StringHandler.isNotBlank(cityName)) {
                res.put("msg", "未找到编码对应的市");
                return res;
            }
            hospital.setCityCode(map.get("cityCode").toString().trim());
            hospital.setCity(cityName);
        }
        if (null != map.get("districtCode") && !"".equals(map.get("districtCode"))) {
            String districtName = this.areaService.getAreaByCode(map.get("districtCode").toString());
            if (!StringHandler.isNotBlank(districtName)) {
                res.put("msg", "未找到编码对应的区");
                return res;
            }
            hospital.setDistrictCode(map.get("districtCode").toString().trim());
            hospital.setDistrict(districtName);
        }
        if (null != map.get("address") && !"".equals(map.get("address"))) {
            hospital.setAddress(map.get("address").toString().trim());
        }
        if (null != map.get("content") && !"".equals(map.get("content"))) {
            hospital.setContent(map.get("content").toString().trim());
        }
        if (null != map.get("imgs") && !"".equals(map.get("imgs"))) {
            hospital.setContent(map.get("imgs").toString().trim());
        }
        Object array = new ArrayList<>();
        if (null != map.get("departCodeArray")) {
            array = map.get("departCodeArray");
        }
        List<String> newIds = JSONArray.parseArray(JSON.toJSON(array).toString(), String.class);
        if (null != map.get("departCodeArray") && !"".equals(map.get("departCodeArray")) && !newIds.isEmpty()) {
            hospitalMapper.updateById(hospital);
            List<HospitalDepartment> hospitalDepartments = hospitalDepartmentService.findDepartCodeAllByHospitalId(map.get("id").toString());
            List<String> ids = hospitalDepartments.stream().map(HospitalDepartment::getDepartCode).collect(Collectors.toList());
            List<String> updateIds = filterList(ids, newIds);
            List<String> insertIds = filterList(newIds, ids);
            if (updateIds.isEmpty()) {
                if (!insertIds.isEmpty()) {
                    insertDepartments(insertIds, map.get("id").toString());
                }
            } else {

                hospitalDepartmentService.deleteByDepartCodes(updateIds, map.get("id").toString());
                if (!insertIds.isEmpty()) {
                    insertDepartments(insertIds, map.get("id").toString());
                }
            }
        } else {
            res.put("msg", "未携带所属科室code");
            return res;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                loginRecordUtil.saveLoginRecord(request, "更新医院详情");
            }
        });
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject hospitalInsert(Map<String, Object> map, HttpServletRequest request) {
        JSONObject res = new JSONObject();
        if (!StringHandler.isNotBlank("departCodeArray")) {
            res.put("msg", "请输入医院所属科室code");
            return res;
        }
        List<String> list = JSONArray.parseArray(JSON.toJSON(map.get("departCodeArray")).toString(), String.class);
        if (!StringHandler.isNotBlank("hosName")) {
            res.put("msg", "请输入医院名称");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("hosGradeCode")))) {
            res.put("msg", "请输入医院等级编码");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("hosLevelCode")))) {
            res.put("msg", "请输入医院级别编码");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("provinceCode")))) {
            res.put("msg", "请输入省code");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("cityCode")))) {
            res.put("msg", "请输入市code");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("districtCode")))) {
            res.put("msg", "请输入区code");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("address")))) {
            res.put("msg", "请输入医院详情地址");
            return res;
        } else if (CollectionUtils.isEmpty(list)) {
            res.put("msg", "请输入医院所属科室code");
            return res;
        }
        List<Hospital> selectList = hospitalMapper.selectList(new QueryWrapper<Hospital>().eq("is_delete", 0));
        List<String> hospitalList = selectList.stream().map(Hospital::getHosName).collect(Collectors.toList());
        if (hospitalList.contains(map.get("hosName").toString())) {
            res.put("msg", "医院名称已存在");
            return res;
        }
        Hospital hospital = new Hospital();
        hospital.setHosName(map.get("hosName").toString().trim());
        String gradeName = this.dictService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_GRADE.getType(), map.get("hosGradeCode").toString());
        if (!StringHandler.isNotBlank(gradeName)) {
            res.put("msg", "未找到编码对应的医院等级");
            return res;
        }
        hospital.setHosGradeCode(map.get("hosGradeCode").toString().trim());
        hospital.setHosGradeName(gradeName);
        String levelName = this.dictService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_LEVEL.getType(), map.get("hosLevelCode").toString());
        if (!StringHandler.isNotBlank(levelName)) {
            res.put("msg", "未找到编码对应的医院级别");
            return res;
        }
        hospital.setHosLevelCode(map.get("hosLevelCode").toString().trim());
        hospital.setHosLevelName(levelName);
        String provinceName = this.areaService.getAreaByCode(map.get("provinceCode").toString());
        if (!StringHandler.isNotBlank(provinceName)) {
            res.put("msg", "未找到编码对应的省");
            return res;
        }
        hospital.setProvinceCode(map.get("provinceCode").toString().trim());
        hospital.setProvince(provinceName);
        String cityName = this.areaService.getAreaByCode(map.get("cityCode").toString());
        if (!StringHandler.isNotBlank(cityName)) {
            res.put("msg", "未找到编码对应的市");
            return res;
        }
        hospital.setCityCode(map.get("cityCode").toString().trim());
        hospital.setCity(cityName);
        String districtName = this.areaService.getAreaByCode(map.get("districtCode").toString());
        if (!StringHandler.isNotBlank(districtName)) {
            res.put("msg", "未找到编码对应的市");
            return res;
        }
        hospital.setDistrictCode(map.get("districtCode").toString().trim());
        hospital.setDistrict(districtName);
        hospital.setAddress(map.get("address").toString().trim());
        hospital.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        if (StringHandler.isNotBlank(String.valueOf(map.get("hosMobile")))) {
            hospital.setHosMobile(map.get("hosMobile").toString().trim());
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("hosCateCode")))) {
            String cateName = this.dictService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_CATE.getType(), map.get("hosCateCode").toString());
            if (!StringHandler.isNotBlank(cateName)) {
                res.put("msg", "未找到编码对应的医院类型");
                return res;
            }
            hospital.setHosCateCode(map.get("hosCateCode").toString().trim());
            hospital.setHosCateName(cateName);
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("hosAttCode")))) {
            String attName = this.dictService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_ATT.getType(), map.get("hosAttCode").toString());
            if (!StringHandler.isNotBlank(attName)) {
                res.put("msg", "未找到编码对应的医院属性");
                return res;
            }
            hospital.setHosAttCode(map.get("hosAttCode").toString().trim());
            hospital.setHosAttName(attName);
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("content")))) {
            hospital.setContent(map.get("content").toString().trim());
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("imgs")))) {
            hospital.setImgs(map.get("imgs").toString().trim());
        }
        hospitalMapper.insert(hospital);
        String hospitalNo = IdUtils.getHospitalID(Integer.parseInt(hospital.getId()));
        Hospital hos = new Hospital();
        hos.setId(hospital.getId());
        hos.setHospitalNo(hospitalNo);
        hospitalMapper.updateById(hos);
        for (String code : list) {
            HospitalDepartment department = new HospitalDepartment();
            department.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
            department.setHospitalId(hospital.getId());
            Department one = departmentService.getOne(new QueryWrapper<Department>().eq("code", code));
            department.setDepartCode(code);
            department.setDepartName(one.getName());
            hospitalDepartmentMapper.insert(department);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                loginRecordUtil.saveLoginRecord(request, "新增医院");
            }
        });
        return res;
    }

    public List<String> filterList(List<String> listA, List<String> listB) {
        List<String> hs1 = new ArrayList<>(listA);
        hs1.removeAll(listB);
        return hs1;
    }

    private void insertDepartments(List<String> insertIds, String hospitalId) {
        for (String code : insertIds) {
            String name = departmentService.findDepartmentNameByCode(code);
            HospitalDepartment department = new HospitalDepartment();
            department.setHospitalId(hospitalId);
            department.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
            department.setDepartCode(code);
            department.setDepartName(name);
            hospitalDepartmentMapper.insert(department);
        }
    }

    @Override
    public JSONArray provincesRelation() {
        List<Area> areas = this.areaService.list();
        Map<String, List<Area>> areasMap = areas.stream().collect(Collectors.groupingBy(Area::getType));
        List<Area> firstLevel = areasMap.get("PROVINCE");
        List<Area> secondLevel = areasMap.get("CITY");
        List<Area> threeLevel = areasMap.get("DISTRICT");
        if (firstLevel == null || secondLevel == null || threeLevel == null) {
            throw new NullPointerException("省市区数据缺失");
        }
        Map<String, List<Area>> secondLevelMap = secondLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        Map<String, List<Area>> threeLevelMap = threeLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        JSONArray respArray = new JSONArray();
        for (Area area : firstLevel) {
            JSONObject resJson = new JSONObject();
            resJson.put("name", area.getName());
            resJson.put("areaCode", area.getAreaCode());
            List<Area> firstSecondRelation = secondLevelMap.get(area.getAreaCode());
            JSONArray secondJsonArray = new JSONArray();
            if (!CollectionUtils.isEmpty(firstSecondRelation)) {
                for (Area s : firstSecondRelation) {
                    JSONObject jsonSecond = new JSONObject();
                    jsonSecond.put("name", s.getName());
                    jsonSecond.put("areaCode", s.getAreaCode());
                    List<Area> secondThreeRelation = threeLevelMap.get(s.getAreaCode());
                    JSONArray threeJsonArray = new JSONArray();
                    if (!CollectionUtils.isEmpty(secondThreeRelation)) {
                        for (Area area1 : secondThreeRelation) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", area1.getName());
                            jsonObject.put("areaCode", area1.getAreaCode());
                            threeJsonArray.add(jsonObject);
                        }
                    }
                    jsonSecond.put("three", threeJsonArray);
                    secondJsonArray.add(jsonSecond);
                }
            }
            resJson.put("second", secondJsonArray);
            respArray.add(resJson);
        }
        return respArray;
    }

    @Override
    public Boolean enableDisable(Map<String, Object> map, HttpServletRequest request) {
        Hospital hospital = new Hospital();
        hospital.setId(map.get("id").toString());
        hospital.setStatus(map.get("status").toString());
        int i = hospitalMapper.updateById(hospital);
        loginRecordUtil.saveLoginRecord(request, "更新医院状态");
        return i > 0;
    }

    @Override
    public Boolean updateDel(Map<String, Object> map, HttpServletRequest request) {
        Hospital hospital = new Hospital();
        hospital.setId(map.get("id").toString());
        hospital.setIsDelete(1);
        int i = hospitalMapper.updateById(hospital);
        loginRecordUtil.saveLoginRecord(request, "删除医院");
        return i > 0;
    }
}
