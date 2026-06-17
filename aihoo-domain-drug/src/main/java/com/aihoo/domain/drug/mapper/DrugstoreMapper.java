package com.aihoo.domain.drug.mapper;

import com.aihoo.domain.drug.entity.Drugstore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 药房 Mapper（迁自 aihoo-core/aihoo-biz-service）。
 *
 * <p>注意：原 DrugstoreMapper 含 {@code selectDrugstorePage}（admin/doctor 用），
 * 迁移时按规则暂不迁入（admin 未迁到前不处理），后续迁移 admin/doctor 时再补。
 */
public interface DrugstoreMapper extends BaseMapper<Drugstore> {
}
