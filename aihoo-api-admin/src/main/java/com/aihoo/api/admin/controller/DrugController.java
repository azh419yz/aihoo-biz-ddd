package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.request.SaveUpdateDrugRequest;
import com.aihoo.api.admin.request.SearchDrugRequest;
import com.aihoo.api.admin.vo.DrugVo;
import com.aihoo.common.*;
import com.aihoo.domain.drug.dto.SaveUpdateDrugRequestDto;
import com.aihoo.domain.drug.entity.Drug;
import com.aihoo.domain.drug.entity.Drugstore;
import com.aihoo.domain.drug.excel.DrugEntity;
import com.aihoo.domain.drug.service.DrugService;
import com.aihoo.domain.drug.service.DrugstoreService;
import com.aihoo.domain.sys.excel.ExcelUtils;
import com.aihoo.exception.BizException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 处方药品管理
 */
@Tag(name = "DrugController", description = "运营端-药品相关接口")
@RestController
@RequestMapping("/api/v1/drug")
@RequiredArgsConstructor
public class DrugController {

    private final DrugService drugService;
    private final DrugstoreService drugstoreService;

    /**
     * 药品管理多条件分页查询
     */
    @Operation(summary = "药品列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PageResult.class},
                            description = "药品列表"
                    )
            )
    )
    @GetMapping("/list")
    public BizResult<PageResult<DrugVo>> list(@ParameterObject PageParam<Drug> pageParam,
                                              @ParameterObject SearchDrugRequest request) {
        PageResult<Drug> page = drugService.getPage(pageParam,
                request == null ? null : request.getDrugstoreId(),
                request == null ? null : request.getName(),
                request == null ? null : request.getInitial());
        return BizResult.success(toDrugVoPage(page));
    }

    /**
     * 药品的启用与禁用
     */
    @Operation(summary = "启用禁用药品")
    @PostMapping("/enableDisable")
    public BizResult<Void> enableDisable(@RequestBody SaveUpdateDrugRequest request) {
        boolean boo = drugService.updateStatus(request.getId(), request.getStatus());
        return boo ? BizResult.success("操作成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "操作失败");
    }

    /**
     * 添加药品
     */
    @Operation(summary = "添加药品")
    @PostMapping("/insert")
    public BizResult<Void> create(@Validated(SaveUpdateDrugRequest.Save.class) @RequestBody SaveUpdateDrugRequest request) {
        SaveUpdateDrugRequestDto dto = new SaveUpdateDrugRequestDto();
        BeanUtils.copyProperties(request, dto);
        boolean result = drugService.create(dto);
        return result ? BizResult.success("添加成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "添加失败");
    }

    /**
     * 修改药品
     */
    @Operation(summary = "修改药品")
    @PutMapping("/update")
    public BizResult<Void> update(@Validated(SaveUpdateDrugRequest.Update.class) @RequestBody SaveUpdateDrugRequest request) {
        SaveUpdateDrugRequestDto dto = new SaveUpdateDrugRequestDto();
        BeanUtils.copyProperties(request, dto);
        boolean result = drugService.update(dto);
        return result ? BizResult.success("更新成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "更新失败");
    }

    /**
     * 删除药品
     */
    @Operation(summary = "删除药品")
    @DeleteMapping("/delete")
    public BizResult<Void> delete(@RequestParam String id) {
        boolean result = drugService.delete(id);
        return result ? BizResult.success("删除成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "删除失败");
    }

    /**
     * 药品excel解析
     */
    @Operation(summary = "药品excel解析")
    @PostMapping("/drugExcelParsing")
    public JsonResult drugExcelParsing(@RequestParam("file") MultipartFile file) {
        try {
            List<DrugEntity> drugEntities = ExcelUtils.readExcel(null, DrugEntity.class, file);
            return JsonResult.ok("解析成功").put("data", drugEntities);
        } catch (Exception e) {
            if (e instanceof BizException) {
                return JsonResult.error(e.getMessage());
            }
            e.printStackTrace();
            return JsonResult.error("解析出错");
        }
    }

    /**
     * 药品批量导出
     */
    @Operation(summary = "药品批量导出")
    @GetMapping("/drugBulkExport")
    public void drugBulkExport(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "manufacturers", required = false) String manufacturers,
                               HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            drugService.drugBulkExport(name, manufacturers, request, response);
        } catch (Exception e) {
            if (e instanceof BizException) {
                throw new BizException(e.getMessage());
            }
            e.printStackTrace();
            try {
                PrintWriter writer = response.getWriter();
                writer.write("{\"msg\":\"导出异常\",\"code\":401}");
                writer.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private PageResult<DrugVo> toDrugVoPage(PageResult<Drug> page) {
        if (page == null || page.getData() == null || page.getData().isEmpty()) {
            return new PageResult<>();
        }
        List<String> drugstoreIds = page.getData().stream()
                .map(Drug::getDrugstoreId)
                .filter(id -> id != null && !id.isEmpty())
                .distinct()
                .toList();
        Map<String, String> drugstoreMap = drugstoreService.listByIds(drugstoreIds).stream()
                .collect(Collectors.toMap(Drugstore::getId, Drugstore::getName, (a, b) -> a));
        List<DrugVo> voList = new ArrayList<>();
        for (Drug drug : page.getData()) {
            DrugVo vo = new DrugVo();
            BeanUtils.copyProperties(drug, vo);
            vo.setDrugstoreName(drugstoreMap.get(drug.getDrugstoreId()));
            voList.add(vo);
        }
        return new PageResult<>(voList, page.getCount());
    }
}
