package com.aihoo.domain.drug.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.drug.entity.Disease;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 疾病 服务接口（d_disease）。
 *
 * <p>原 aihoo-biz-service/aihoo-admin 的 DiseaseService，迁至 drug 域。
 * <p>API 入参沿用 Map（与原 admin 保持一致），按 CLAUDE.md 规则 1 不擅自重构。
 */
public interface DiseaseService extends IService<Disease> {

    /**
     * 疾病分页列表（admin: DiseaseController.list）。
     */
    PageResult<Disease> diseaseList(Map<String, Object> map);

    /**
     * 疾病新增（admin: DiseaseController.add）。
     */
    boolean addDisease(Map<String, Object> map);

    /**
     * 疾病启用禁用（admin: DiseaseController.diseaseEnableDisable）。
     *
     * <p>注意：原 admin 端点写 isDelete 字段（1=删除 0=可用），与常规 status 不同。
     */
    boolean updateDisease(Map<String, Object> map);
}
