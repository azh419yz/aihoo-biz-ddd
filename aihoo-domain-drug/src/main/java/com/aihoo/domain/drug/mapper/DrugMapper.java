package com.aihoo.domain.drug.mapper;

import com.aihoo.domain.drug.entity.Drug;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 药品 Mapper（迁自 patient-api 的 DrugMapper）。
 *
 * <p>doctor-api 的同名 DrugMapper 字段更多（含 drugstoreId/method/pinyinInitial 等），
 * 后续迁 doctor-api 时按需扩展。
 */
public interface DrugMapper extends BaseMapper<Drug> {
}
