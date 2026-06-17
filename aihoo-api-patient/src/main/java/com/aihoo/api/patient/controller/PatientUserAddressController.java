package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.PatientUserAddressDeleteRequest;
import com.aihoo.api.patient.request.PatientUserAddressSaveRequest;
import com.aihoo.api.patient.request.PatientUserAddressSelectRequest;
import com.aihoo.api.patient.request.PatientUserAddressUpdateRequest;
import com.aihoo.common.BizResult;
import com.aihoo.domain.patient.dto.PatientUserAddressSaveDto;
import com.aihoo.domain.patient.dto.PatientUserAddressSelectDto;
import com.aihoo.domain.patient.dto.PatientUserAddressUpdateDto;
import com.aihoo.domain.patient.entity.PatientUserAddress;
import com.aihoo.domain.patient.service.PatientUserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PatientUserAddress", description = "患者端-收货地址相关接口")
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/patient-user-address")
public class PatientUserAddressController {

    private final PatientUserAddressService patientUserAddressService;

    @PostMapping
    @Operation(summary = "保存用户地址")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Boolean.class},
                            description = "保存用户地址"
                    )
            )
    )
    public BizResult<Boolean> saveAddress(@RequestBody PatientUserAddressSaveRequest saveRequest) {
        try {
            PatientUserAddressSaveDto dto = new PatientUserAddressSaveDto();
            BeanUtils.copyProperties(saveRequest, dto);
            return BizResult.success(patientUserAddressService.saveAddress(dto));
        } catch (Exception e) {
            log.info("异常:", e);
        }
        return BizResult.fail(500, "系统异常");
    }

    @PutMapping
    @Operation(summary = "更新用户地址")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Boolean.class},
                            description = "更新用户地址"
                    )
            )
    )
    public BizResult<Boolean> updateAddress(@RequestBody PatientUserAddressUpdateRequest updateRequest) {
        try {
            PatientUserAddressUpdateDto dto = new PatientUserAddressUpdateDto();
            BeanUtils.copyProperties(updateRequest, dto);
            return BizResult.success(patientUserAddressService.updateAddress(dto));
        } catch (Exception e) {
            log.info("异常:", e);
        }
        return BizResult.fail(500, "系统异常");
    }

    @DeleteMapping
    @Operation(summary = "删除用户地址")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Boolean.class},
                            description = "删除用户地址"
                    )
            )
    )
    public BizResult<Boolean> deleteAddress(@RequestBody PatientUserAddressDeleteRequest deleteRequest) {
        try {
            return BizResult.success(patientUserAddressService.removeById(deleteRequest.getId()));
        } catch (Exception e) {
            log.info("异常:", e);
        }
        return BizResult.fail(500, "系统异常");
    }

    @GetMapping
    @Operation(summary = "查询用户地址")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PatientUserAddress.class},
                            description = "查询用户地址"
                    )
            )
    )
    public BizResult<List<PatientUserAddress>> selectAddress(PatientUserAddressSelectRequest selectRequest) {
        try {
            PatientUserAddressSelectDto dto = new PatientUserAddressSelectDto();
            BeanUtils.copyProperties(selectRequest, dto);
            return BizResult.success(patientUserAddressService.selectAddress(dto));
        } catch (Exception e) {
            log.info("异常:", e);
        }
        return BizResult.fail(500, "系统异常");
    }

    @GetMapping("/default")
    @Operation(summary = "查询默认用户地址")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PatientUserAddress.class},
                            description = "查询默认用户地址"
                    )
            )
    )
    public BizResult<PatientUserAddress> selectDefaultAddress(PatientUserAddressSelectRequest selectRequest) {
        try {
            PatientUserAddressSelectDto dto = new PatientUserAddressSelectDto();
            BeanUtils.copyProperties(selectRequest, dto);
            return BizResult.success(patientUserAddressService.selectDefaultAddress(dto));
        } catch (Exception e) {
            log.info("异常:", e);
        }
        return BizResult.fail(500, "系统异常");
    }
}
