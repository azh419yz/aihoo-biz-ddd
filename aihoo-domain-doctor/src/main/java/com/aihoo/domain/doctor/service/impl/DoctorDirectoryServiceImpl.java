package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.dto.DoctorDirectoryDto;
import com.aihoo.domain.doctor.entity.DoctorDirectory;
import com.aihoo.domain.doctor.mapper.DoctorDirectoryMapper;
import com.aihoo.domain.doctor.service.DoctorDirectoryService;
import com.aihoo.domain.patient.entity.HosSick;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.sys.oss.OssComponent;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 医生通讯录 service 实现（迁自 doctor-api: DoctorDirectoryServiceImpl）。
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DoctorDirectoryServiceImpl extends ServiceImpl<DoctorDirectoryMapper, DoctorDirectory> implements DoctorDirectoryService {

    private final HosSickService hosSickService;
    private final OssComponent ossComponent;

    @Override
    public List<DoctorDirectoryDto> findDoctorDirectoryList(String sickName) {
        IPage<DoctorDirectory> page = new Page<>(1, 50);
        QueryWrapper<DoctorDirectory> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(sickName), "sick_name", sickName);
        queryWrapper.eq("doctor_id", 42);
        queryWrapper.orderByDesc("create_time");
        List<DoctorDirectory> list = baseMapper.selectPage(page, queryWrapper).getRecords();

        List<Long> sickId = list.stream().map(DoctorDirectory::getSickId).toList();

        List<HosSick> sickList = hosSickService.listBySickIds(sickId);

        return list.stream().map(dir -> {
            List<HosSick> hosSick = sickList
                    .stream()
                    .filter(sick -> Long.valueOf(sick.getId()).equals(dir.getSickId()))
                    .toList();
            DoctorDirectoryDto dto = new DoctorDirectoryDto();

            if (CollectionUtils.isEmpty(hosSick)) {
                dto.setSickName("患者" + new Random().nextInt(9000));
                return dto;
            }

            HosSick sick = hosSick.get(0);

            dto.setSickAge(sick.getAge());
            dto.setMobile(sick.getMobile());
            dto.setSource(dir.getSource());
            dto.setSickId(dir.getSickId());
            dto.setPatientUserId(dir.getPatientUserId());
            dto.setSickSex(sick.getSex());
            dto.setSickName(sick.getName());
            dto.setSaveTime(sick.getCreateTime());
            dto.setAvatar(ossComponent.getUrl(getAvatarPath(sick.getSex(), sick.getAge())));
            return dto;
        }).toList();
    }

    private String getAvatarPath(String sex, String ageStr) {
        String genderPrefix = "1".equals(sex) ? "M" : "W";
        int age = 0;
        try {
            if (StringUtil.isNotBlank(ageStr)) {
                String numericAge = ageStr.replaceAll("\\D+", "");
                if (StringUtil.isNotBlank(numericAge)) {
                    age = Integer.parseInt(numericAge);
                }
            }
        } catch (Exception e) {
            // ignore parse error, default to 0
        }

        String ageSuffix;
        if (age <= 6) {
            ageSuffix = "1";
        } else if (age <= 20) {
            ageSuffix = "2";
        } else if (age <= 60) {
            ageSuffix = "3";
        } else {
            ageSuffix = "4";
        }

        return "patient_aihoo/avatar/" + genderPrefix + ageSuffix + ".jpg";
    }
}
