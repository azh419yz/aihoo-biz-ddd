package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.vo.TBaseVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.dto.TBaseDto;
import com.aihoo.domain.sys.service.TBaseService;
import com.aihoo.util.StringHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "CommonWords", description = "常用语配置")
@RestController
@RequestMapping("/api/v1/commonWords")
@RequiredArgsConstructor
public class CommonWordsController {

    private final TBaseService tBaseService;

    @PostMapping("/page")
    public BizResult<List<TBaseVo>> page() {
        List<TBaseDto> dtos = tBaseService.pageList();
        return BizResult.success(dtos.stream().map(this::toVo).collect(Collectors.toList()));
    }

    @PostMapping("/save")
    public BizResult<Void> save(@RequestBody Map<String, Object> map) {
        if (StringHandler.isEmpty(String.valueOf(map.get("content")))
                || StringHandler.isEmpty(String.valueOf(map.get("index")))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "参数不能为空");
        }
        boolean ok = tBaseService.addCommonWords(map);
        return ok ? BizResult.success() : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "新增常用语失败");
    }

    @PostMapping("/update")
    public BizResult<Void> update(@RequestBody Map<String, Object> map) {
        if (StringHandler.isEmpty(String.valueOf(map.get("id")))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "id不能为空");
        }
        if (StringHandler.isEmpty(String.valueOf(map.get("content")))
                && StringHandler.isEmpty(String.valueOf(map.get("index")))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "参数不能为空");
        }
        boolean ok = tBaseService.updateCommonWords(map);
        return ok ? BizResult.success() : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "更新常用语失败");
    }

    @PostMapping("/delete")
    public BizResult<Void> delete(@RequestBody Map<String, Object> map) {
        if (StringHandler.isEmpty(String.valueOf(map.get("id")))) {
            return BizResult.fail(com.aihoo.common.BizResultCode.BAD_REQUEST, "id不能为空");
        }
        boolean ok = tBaseService.removeById(String.valueOf(map.get("id")));
        return ok ? BizResult.success() : BizResult.fail(com.aihoo.common.BizResultCode.OPERATION_FAILED, "删除常用语失败");
    }

    private TBaseVo toVo(TBaseDto dto) {
        TBaseVo vo = new TBaseVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
