package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.ImAddGroupMemberRequest;
import com.aihoo.api.patient.request.ImCreateGroupRequest;
import com.aihoo.api.patient.request.ImDeleteGroupMemberRequest;
import com.aihoo.api.patient.request.ImGetGroupMemberInfoRequest;
import com.aihoo.api.patient.request.ImSendGroupMsgRequest;
import com.aihoo.api.patient.vo.ImAddGroupMemberRespVo;
import com.aihoo.api.patient.vo.ImCreateGroupRespVo;
import com.aihoo.api.patient.vo.ImGetGroupMemberInfoRespVo;
import com.aihoo.api.patient.vo.ImRespVo;
import com.aihoo.api.patient.vo.ImSendGroupMsgRespVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.im.dto.ImAddGroupMemberRequestDto;
import com.aihoo.domain.im.dto.ImCreateGroupRequestDto;
import com.aihoo.domain.im.dto.ImDeleteGroupMemberRequestDto;
import com.aihoo.domain.im.dto.ImGetGroupMemberInfoRequestDto;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.entity.ImGroup;
import com.aihoo.domain.im.service.ImGroupService;
import com.aihoo.domain.im.util.TencentImGroupUtil;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ImGroupV2", description = "患者端-im-group相关接口")
@Slf4j
@RestController
@RequestMapping("/api/v2/im-group")
@RequiredArgsConstructor
public class ImGroupController {

    private final ImGroupService imGroupService;
    private final TencentImGroupUtil tencentImGroupUtil;

    @PostMapping
    public BizResult<ImCreateGroupRespVo> createGroup(@RequestBody ImCreateGroupRequest imCreateGroupRequest) {
        ImCreateGroupRequestDto dto = new ImCreateGroupRequestDto();
        BeanUtils.copyProperties(imCreateGroupRequest, dto);
        com.aihoo.domain.im.dto.ImCreateGroupRespVo resp = tencentImGroupUtil.createGroup(dto);
        if (resp.isSuccess()) {
            ImGroup imGroup = new ImGroup();
            imGroup.setGroupId(resp.getGroupId());
            imGroup.setGroupType(imCreateGroupRequest.getType());
            imGroup.setGroupOwnerAccount(imCreateGroupRequest.getOwnerAccount());
            imGroupService.save(imGroup);
            return BizResult.success(toApiVo(resp));
        }
        log.info("创建分组失败,参数:{},返回对象:{}",
                JSONObject.toJSONString(imCreateGroupRequest), JSONObject.toJSONString(resp));
        return BizResult.fail(500, "请求异常.");
    }

    @PostMapping("/member")
    public BizResult<ImAddGroupMemberRespVo> addGroupMember(@RequestBody ImAddGroupMemberRequest req) {
        ImAddGroupMemberRequestDto dto = new ImAddGroupMemberRequestDto();
        BeanUtils.copyProperties(req, dto);
        com.aihoo.domain.im.dto.ImAddGroupMemberRespVo resp = tencentImGroupUtil.addGroupMember(dto);
        if (resp.isSuccess()) {
            return BizResult.success(toAddVo(resp));
        }
        log.info("新增用户失败,参数:{},返回对象:{}",
                JSONObject.toJSONString(req), JSONObject.toJSONString(resp));
        return BizResult.fail(500, "请求异常.");
    }

    @DeleteMapping("/member")
    public BizResult<ImRespVo> deleteGroupMember(@RequestBody ImDeleteGroupMemberRequest req) {
        ImDeleteGroupMemberRequestDto dto = new ImDeleteGroupMemberRequestDto();
        BeanUtils.copyProperties(req, dto);
        com.aihoo.domain.im.dto.ImRespVo resp = tencentImGroupUtil.deleteGroupMember(dto);
        if (resp.isSuccess()) {
            return BizResult.success(toRespVo(resp));
        }
        log.info("删除用户失败,参数:{},返回对象:{}", JSONObject.toJSONString(req),
                JSONObject.toJSONString(resp));
        return BizResult.fail(500, "请求异常.");
    }

    @PostMapping("/msg")
    public BizResult<ImRespVo> sendGroupMsg(@RequestBody ImSendGroupMsgRequest req) {
        req.setMsgType(1);
        ImSendGroupMsgRequestDto dto = new ImSendGroupMsgRequestDto();
        BeanUtils.copyProperties(req, dto);
        com.aihoo.domain.im.dto.ImSendGroupMsgRespVo resp = imGroupService.sendMsg(dto);
        if (resp.isSuccess()) {
            return BizResult.success(toSendVo(resp));
        }
        log.info("发送消息失败,参数:{},返回对象:{}", JSONObject.toJSONString(req),
                JSONObject.toJSONString(resp));
        return BizResult.fail(500, "请求异常.");
    }

    @GetMapping("/member")
    public BizResult<ImGetGroupMemberInfoRespVo> getGroupMember(ImGetGroupMemberInfoRequest req) {
        ImGetGroupMemberInfoRequestDto dto = new ImGetGroupMemberInfoRequestDto();
        BeanUtils.copyProperties(req, dto);
        com.aihoo.domain.im.dto.ImGetGroupMemberInfoRespVo resp = tencentImGroupUtil.getGroupMemberInfo(dto);
        if (resp.isSuccess()) {
            return BizResult.success(toGetVo(resp));
        }
        log.info("获取用户列表失败,参数:{},返回对象:{}",
                JSONObject.toJSONString(req), JSONObject.toJSONString(resp));
        return BizResult.fail(500, "请求异常.");
    }

    private ImCreateGroupRespVo toApiVo(com.aihoo.domain.im.dto.ImCreateGroupRespVo dto) {
        if (dto == null) return null;
        ImCreateGroupRespVo vo = new ImCreateGroupRespVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private ImAddGroupMemberRespVo toAddVo(com.aihoo.domain.im.dto.ImAddGroupMemberRespVo dto) {
        if (dto == null) return null;
        ImAddGroupMemberRespVo vo = new ImAddGroupMemberRespVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private ImGetGroupMemberInfoRespVo toGetVo(com.aihoo.domain.im.dto.ImGetGroupMemberInfoRespVo dto) {
        if (dto == null) return null;
        ImGetGroupMemberInfoRespVo vo = new ImGetGroupMemberInfoRespVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private ImSendGroupMsgRespVo toSendVo(com.aihoo.domain.im.dto.ImSendGroupMsgRespVo dto) {
        if (dto == null) return null;
        ImSendGroupMsgRespVo vo = new ImSendGroupMsgRespVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private ImRespVo toRespVo(com.aihoo.domain.im.dto.ImRespVo dto) {
        if (dto == null) return null;
        ImRespVo vo = new ImRespVo();
        vo.setActionStatus(dto.getActionStatus());
        vo.setErrorCode(dto.getErrorCode());
        vo.setErrorInfo(dto.getErrorInfo());
        return vo;
    }
}
