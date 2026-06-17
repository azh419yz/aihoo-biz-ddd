package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.dto.DictTypeItemDto;
import com.aihoo.domain.sys.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 字典 service。
 *
 * <p>旧 DiceService 改名 DictService（与 entity Dict 对齐）。
 * controller 用到的方法：getDoctorType、listByType
 */
public interface DictService extends IService<Dict> {

    List<DictTypeItemDto> getDoctorType(String type);

    List<Map<String, String>> listByType(String type);
}
