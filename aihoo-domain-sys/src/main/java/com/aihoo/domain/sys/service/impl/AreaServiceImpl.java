package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.dto.AreaDto;
import com.aihoo.domain.sys.entity.Area;
import com.aihoo.domain.sys.mapper.AreaMapper;
import com.aihoo.domain.sys.service.AreaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final AreaMapper areaMapper;

    @Override
    public String getAreaByCode(String code) {
        QueryWrapper<Area> wrapper = new QueryWrapper<>();
        wrapper.eq("area_code", code);
        return this.areaMapper.selectList(wrapper).get(0).getName();
    }

    @Override
    public List<AreaDto> provincesRelation() {
        return l3List();
    }

    @Override
    public List<AreaDto> doctorProCityList() {
        return l2List();
    }

    @Override
    public List<AreaDto> l3List() {
        List<Area> areas = this.areaMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(areas)) {
            return List.of();
        }
        Map<String, List<Area>> areasMap = areas.stream().collect(Collectors.groupingBy(Area::getType));
        List<Area> firstLevel = null;
        List<Area> secondLevel = null;
        List<Area> threeLevel = null;
        if (CollectionUtils.isEmpty(areasMap)) {
            return List.of();
        }
        for (Map.Entry<String, List<Area>> entry : areasMap.entrySet()) {
            if ("PROVINCE".equals(entry.getKey())) {
                firstLevel = entry.getValue();
            }
            if ("CITY".equals(entry.getKey())) {
                secondLevel = entry.getValue();
            }
            if ("DISTRICT".equals(entry.getKey())) {
                threeLevel = entry.getValue();
            }
        }
        assert firstLevel != null;
        assert secondLevel != null;
        assert threeLevel != null;
        Map<String, List<Area>> secondLevelMap = secondLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        Map<String, List<Area>> threeLevelMap = threeLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        List<AreaDto> respArray = new ArrayList<>();
        firstLevel.forEach(area -> {
            AreaDto resJson = new AreaDto();
            resJson.setName(area.getName());
            resJson.setAreaCode(area.getAreaCode());
            List<Area> firstSecondRelation = secondLevelMap.get(area.getAreaCode());
            List<AreaDto> secondJsonArray = new ArrayList<>();
            if (!CollectionUtils.isEmpty(firstSecondRelation)) {
                firstSecondRelation.forEach(s -> {
                    AreaDto jsonSecond = new AreaDto();
                    jsonSecond.setName(s.getName());
                    jsonSecond.setAreaCode(s.getAreaCode());
                    List<Area> secondThreeRelation = threeLevelMap.get(s.getAreaCode());
                    List<AreaDto> threeJsonArray = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(secondThreeRelation)) {
                        secondThreeRelation.forEach(area1 -> {
                            AreaDto jsonObject = new AreaDto();
                            jsonObject.setName(area1.getName());
                            jsonObject.setAreaCode(area1.getAreaCode());
                            threeJsonArray.add(jsonObject);
                        });
                    }
                    jsonSecond.setChildren(threeJsonArray);
                    secondJsonArray.add(jsonSecond);
                });
            }
            resJson.setChildren(secondJsonArray);
            respArray.add(resJson);
        });
        return respArray;
    }

    @Override
    public List<AreaDto> l2List() {
        List<Area> areas = this.areaMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(areas)) {
            return List.of();
        }
        Map<String, List<Area>> areasMap = areas.stream().collect(Collectors.groupingBy(Area::getType));
        if (CollectionUtils.isEmpty(areasMap)) {
            return List.of();
        }
        List<Area> firstLevel = null;
        List<Area> secondLevel = null;
        for (Map.Entry<String, List<Area>> entry : areasMap.entrySet()) {
            if ("PROVINCE".equals(entry.getKey())) {
                firstLevel = entry.getValue();
            }
            if ("CITY".equals(entry.getKey())) {
                secondLevel = entry.getValue();
            }
        }
        assert firstLevel != null;
        assert secondLevel != null;
        Map<String, List<Area>> secondLevelMap = secondLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        List<AreaDto> respArray = new ArrayList<>();
        firstLevel.forEach(area -> {
            AreaDto resJson = new AreaDto();
            resJson.setName(area.getName());
            resJson.setAreaCode(area.getAreaCode());
            List<Area> firstSecondRelation = secondLevelMap.get(area.getAreaCode());
            List<AreaDto> secondJsonArray = new ArrayList<>();
            if (!CollectionUtils.isEmpty(firstSecondRelation)) {
                firstSecondRelation.forEach(s -> {
                    AreaDto jsonSecond = new AreaDto();
                    jsonSecond.setName(s.getName());
                    jsonSecond.setAreaCode(s.getAreaCode());
                    secondJsonArray.add(jsonSecond);
                });
            }
            resJson.setChildren(secondJsonArray);
            respArray.add(resJson);
        });
        return respArray;
    }

    @Override
    public List<Area> provincesList() {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>().eq(Area::getType, "PROVINCE"));
    }

    @Override
    public List<Area> cityList(String parentAreaCode) {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>()
                .eq(Area::getType, "CITY")
                .eq(Area::getParentAreaCode, parentAreaCode));
    }

    @Override
    public List<Area> districtList(String parentAreaCode) {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>()
                .eq(Area::getType, "DISTRICT")
                .eq(Area::getParentAreaCode, parentAreaCode));
    }
}