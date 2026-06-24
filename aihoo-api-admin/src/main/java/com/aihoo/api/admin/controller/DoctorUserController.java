package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.request.DoctorEnableDisableRequest;
import com.aihoo.api.admin.request.DoctorUserAddRequest;
import com.aihoo.api.admin.request.DoctorUserUpdateRequest;
import com.aihoo.api.admin.vo.DoctorUserVo;
import com.aihoo.api.admin.vo.HospitalSimpleVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.dto.DoctorEnableDisableRequestDto;
import com.aihoo.domain.doctor.dto.DoctorUserAddRequestDto;
import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.dto.DoctorUserUpdateRequestDto;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.hospital.entity.Hospital;
import com.aihoo.domain.hospital.service.HospitalService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "DoctorUser", description = "运营端-医生管理")
@RestController
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
public class DoctorUserController {

    private final DoctorUserService doctorUserService;
    private final HospitalService hospitalService;

    @Operation(summary = "医生新增")
    @PostMapping("/add")
    public BizResult<Void> doctorUserAdd(@Valid @RequestBody DoctorUserAddRequest request, HttpServletRequest httpRequest) throws Exception {
        DoctorUserAddRequestDto dto = new DoctorUserAddRequestDto();
        BeanUtils.copyProperties(request, dto);
        doctorUserService.doctorUserAdd(dto, httpRequest);
        return BizResult.success("新增成功");
    }

    @Operation(summary = "医生更新")
    @PostMapping("/update")
    public BizResult<Void> doctorUpdate(@Valid @RequestBody DoctorUserUpdateRequest request, HttpServletRequest httpRequest) throws Exception {
        DoctorUserUpdateRequestDto dto = new DoctorUserUpdateRequestDto();
        BeanUtils.copyProperties(request, dto);
        doctorUserService.doctorUpdate(dto, httpRequest);
        return BizResult.success("更新成功");
    }

    @Operation(summary = "医生详情")
    @PostMapping("/doctorDetails")
    public BizResult<DoctorUserVo> doctorDetails(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null || "".equals(map.get("id").toString())) {
            return BizResult.fail(BizResultCode.BAD_REQUEST, "未携带医生的id");
        }
        DoctorUserDetailsDto details = doctorUserService.doctorDetails(map.get("id").toString());
        if (details == null) {
            return BizResult.fail(BizResultCode.NOT_FOUND, "没有对应id的详情");
        }
        DoctorUserVo vo = new DoctorUserVo();
        BeanUtils.copyProperties(details, vo);
        return BizResult.success(vo);
    }

    @Operation(summary = "医生列表")
    @PostMapping("/list")
    public BizResult<PageResult<DoctorUserVo>> list(@RequestBody Map<String, Object> map) {
        PageResult<DoctorUser> page = doctorUserService.list(map);
        List<DoctorUserVo> data = new ArrayList<>();
        if (page.getData() != null) {
            for (DoctorUser u : page.getData()) {
                DoctorUserVo vo = new DoctorUserVo();
                BeanUtils.copyProperties(u, vo);
                data.add(vo);
            }
        }
        return BizResult.success(new PageResult<>(data, page.getCount()));
    }

    @Operation(summary = "已存在医院")
    @PostMapping("/existHospitalAll")
    public BizResult<List<HospitalSimpleVo>> getExistHospitalAll() {
        List<Hospital> list = hospitalService.list(new QueryWrapper<Hospital>().eq("status", 1).eq("is_delete", 0));
        List<HospitalSimpleVo> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (Hospital h : list) {
                HospitalSimpleVo vo = new HospitalSimpleVo();
                vo.setId(h.getId());
                vo.setHosName(h.getHosName());
                result.add(vo);
            }
        }
        return BizResult.success(result);
    }

    @Operation(summary = "医院下所有科室")
    @PostMapping("/get/hospitalDepartmentAll")
    public BizResult<Object> hospitalDepartmentAll(@RequestBody Map<String, Object> map) {
        if (map.get("hospitalId") == null || "".equals(map.get("hospitalId"))) {
            return BizResult.fail(BizResultCode.BAD_REQUEST, "未携带医院id");
        }
        return BizResult.success(doctorUserService.hospitalDepartmentAll(map.get("hospitalId").toString()));
    }

    @Operation(summary = "医生启用与禁用")
    @PostMapping("/enableDisable")
    public BizResult<Void> enableDisable(@Valid @RequestBody DoctorEnableDisableRequest request) {
        DoctorEnableDisableRequestDto dto = new DoctorEnableDisableRequestDto();
        BeanUtils.copyProperties(request, dto);
        boolean ok = doctorUserService.enableDisable(dto);
        return ok ? BizResult.success("更新成功") : BizResult.fail(BizResultCode.NOT_FOUND, "不存在的id" + request.getId());
    }
}