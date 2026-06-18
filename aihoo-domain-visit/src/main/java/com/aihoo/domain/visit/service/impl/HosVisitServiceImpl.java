package com.aihoo.domain.visit.service.impl;


import com.aihoo.constant.PushMessageType;
import com.aihoo.domain.doctor.entity.DoctorDirectory;
import com.aihoo.domain.doctor.entity.DoctorWelcomeMessageSet;
import com.aihoo.domain.doctor.service.DoctorDirectoryService;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.doctor.service.DoctorWelcomeMessageSetService;
import com.aihoo.domain.im.dto.ImCreateGroupRequestDto;
import com.aihoo.domain.im.dto.ImCreateGroupRespVo;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.entity.ImGroup;
import com.aihoo.domain.im.entity.ImGroupMember;
import com.aihoo.domain.im.entity.ImMsg;
import com.aihoo.domain.im.entity.ImMsgContent;
import com.aihoo.domain.im.mapper.ImMsgMapper;
import com.aihoo.domain.im.service.ImGroupMemberService;
import com.aihoo.domain.im.service.ImGroupService;
import com.aihoo.domain.im.service.ImMsgContentService;
import com.aihoo.domain.im.util.TencentImGroupUtil;
import com.aihoo.domain.patient.entity.HosSick;
import com.aihoo.domain.patient.entity.HosSickHealthRecords;
import com.aihoo.domain.patient.entity.PatientUser;
import com.aihoo.domain.patient.mapper.HosSickMapper;
import com.aihoo.domain.patient.mapper.PatientUserMapper;
import com.aihoo.domain.patient.service.HosSickHealthRecordsService;
import com.aihoo.domain.sys.oss.OssComponent;
import com.aihoo.domain.visit.dto.*;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.entity.HosVisitImg;
import com.aihoo.domain.visit.entity.HosVisitLog;
import com.aihoo.domain.visit.entity.Order;
import com.aihoo.domain.visit.mapper.HosVisitImgMapper;
import com.aihoo.domain.visit.mapper.HosVisitLogMapper;
import com.aihoo.domain.visit.mapper.HosVisitMapper;
import com.aihoo.domain.visit.mapper.HosVisitVoMapper;
import com.aihoo.domain.visit.mapper.OrderMapper;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.domain.visit.util.VisitStatusUtil;
import com.aihoo.exception.BizException;
import com.aihoo.push.PushMessageService;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.OrderNoUtil;
import com.aihoo.util.AvatarUtil;
import com.aihoo.util.StringUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Service
public class HosVisitServiceImpl extends ServiceImpl<HosVisitMapper, HosVisit> implements HosVisitService {

    @Resource
    private HosSickMapper hosSickMapper;
    @Resource
    private PatientUserMapper patientUserMapper;
    @Resource
    private HosVisitMapper hosVisitMapper;
    @Resource
    private HosVisitImgMapper hosVisitImgMapper;
    @Resource
    private HosVisitVoMapper hosVisitVoMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private HosVisitLogMapper hosVisitLogMapper;
    @Autowired
    private PushMessageService pushMessageServiceImpl;
    @Autowired
    private DoctorUserService doctorUserService;
    @Autowired
    private DoctorWelcomeMessageSetService doctorWelcomeMessageSetService;
    @Autowired
    private ImMsgMapper imMsgMapper;
    @Autowired
    private ImMsgContentService imMsgContentService;
    @Autowired
    private OssComponent ossComponent;

    @Autowired
    private HosSickHealthRecordsService hosSickHealthRecordsService;

    @Autowired
    private DoctorDirectoryService doctorDirectoryService;
    @Autowired
    private TencentImGroupUtil tencentImGroupUtil;

    @Autowired
    private ImGroupService imGroupService;

    @Autowired
    private ImGroupMemberService imGroupMemberService;

    @Override
    public JSONArray patientList(Map<String, Object> map) {
        map.put("patientUserId", AuthUtil.getLoginUserId());
        List<com.aihoo.domain.visit.dto.HosVisitVo> hosVisitVoList = hosVisitVoMapper.patientList(map);
        if (CollectionUtils.isEmpty(hosVisitVoList)) {
            return new JSONArray();
        }
        JSONArray jSONArray = new JSONArray();

        for (com.aihoo.domain.visit.dto.HosVisitVo hosVisitVo : hosVisitVoList) {
            JSONObject jsonObject = new JSONObject();

            String status = hosVisitVo.getStatus();
            String type = hosVisitVo.getType();
            String doctorUserId = hosVisitVo.getDoctorUserId();
            jsonObject.put("doctorId", doctorUserId);
            jsonObject.put("fiveStar", hosVisitVo.getFiveStar());
            jsonObject.put("doctorName", hosVisitVo.getDoctorName());
            jsonObject.put("doctorHeadImg", hosVisitVo.getDoctorHeadImg());
            jsonObject.put("hospitalName", hosVisitVo.getHospitalName());
            jsonObject.put("officeHolderName", hosVisitVo.getOfficeHolderName());
            jsonObject.put("departName", hosVisitVo.getDepartName());
            jsonObject.put("totalPrice", hosVisitVo.getTotalPrice());
            jsonObject.put("orderNum", hosVisitVo.getOrderNum());
            jsonObject.put("type", type);
            jsonObject.put("status", status);
            jsonObject.put("age", hosVisitVo.getAge());
            jsonObject.put("patientId", hosVisitVo.getPatientUserId());
            jsonObject.put("sex", hosVisitVo.getSex());
            jsonObject.put("sickId", hosVisitVo.getHosSickId());
            jsonObject.put("sickName", hosVisitVo.getSickName());
            jsonObject.put("createTime", hosVisitVo.getCreateTime());
            jsonObject.put("startTime", hosVisitVo.getStartTime());
            jsonObject.put("id", hosVisitVo.getId());
            jsonObject.put("content", hosVisitVo.getContent());
            jsonObject.put("msg", hosVisitVo.getMsg());
            jsonObject.put("imUserId", hosVisitVo.getImUserId());
            jsonObject.put("imUserSig", hosVisitVo.getImUserSig());
            jsonObject.put("avatar", ossComponent.getUrl(getAvatarPath(hosVisitVo.getSex(), hosVisitVo.getAge())));
            jsonObject.put("imGroupId", "GROUP_" + hosVisitVo.getOrderNum());
            jsonObject.put("visitStatus", JSONObject.toJSONString(VisitStatusUtil.getStatusFlow(status)));
            QueryWrapper<ImMsg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_num", hosVisitVo.getOrderNum());
            queryWrapper.orderByDesc("create_time");
            List<ImMsg> msgList = imMsgMapper.selectList(queryWrapper);
            Integer count = msgList.stream().filter(msg -> msg.getSickPeerReadStatus() == 0).toList().size();
            jsonObject.put("msgPeerReadCount", count);

            if (!CollectionUtils.isEmpty(msgList)) {
                ImMsg lastMsg = msgList.stream()
                        .filter(msg -> msg.getFromAccount() == null || !msg.getFromAccount().startsWith("PATIENT_"))
                        .findFirst()
                        .orElse(null);
                if (lastMsg != null) {
                    jsonObject.put("lastMsgStatus", lastMsg.getMsgStatus());
                    String senderRole = (lastMsg.getFromAccount() != null && lastMsg.getFromAccount().startsWith("DOCTOR_")) ? "doctor" : "";
                    jsonObject.put("lastMsgSenderRole", senderRole);
                    if ("WITHDRAW".equals(lastMsg.getMsgStatus())) {
                        jsonObject.put("lastMsgType", lastMsg.getMsgType());
                        jsonObject.put("lastMsgContent", "");
                    } else {
                        ImMsgContent msgContent = imMsgContentService.getOne(new LambdaQueryWrapper<ImMsgContent>()
                                .eq(ImMsgContent::getImMsgId, lastMsg.getId())
                                .orderByDesc(ImMsgContent::getCreateTime)
                                .last("LIMIT 1"));
                        if (msgContent != null) {
                            jsonObject.put("lastMsgType", msgContent.getMsgType());
                            jsonObject.put("lastMsgContent", msgContent.getMsgContent());
                        } else {
                            jsonObject.put("lastMsgType", lastMsg.getMsgType());
                            jsonObject.put("lastMsgContent", "");
                        }
                    }
                } else {
                    jsonObject.put("lastMsgStatus", "");
                    jsonObject.put("lastMsgType", "");
                    jsonObject.put("lastMsgContent", "");
                    jsonObject.put("lastMsgSenderRole", "");
                }
            } else {
                jsonObject.put("lastMsgStatus", "");
                jsonObject.put("lastMsgType", "");
                jsonObject.put("lastMsgContent", "");
                jsonObject.put("lastMsgSenderRole", "");
            }

            jsonObject.put("hasHealthInfo", hosVisitVo.getHasHealthInfo());
            jsonObject.put("hasBaseInfo", hosVisitVo.getHasBaseInfo());
            jSONArray.add(jsonObject);
        }
        return jSONArray;
    }

    private boolean serviceVisitRemind(HosVisit hosVisit) {
        try {
            pushMessageServiceImpl.insertOne("在线复诊订单", "您已取消在线复诊，退回" + hosVisit.getPayType() + "平台" + hosVisit.getTotalPrice() + "，退款预计2~5个工作日到账。",
                    PushMessageType.messageType_IMAGE, hosVisit.getId(), "您已取消在线复诊，退回" + hosVisit.getPayType() + "平台" + hosVisit.getTotalPrice() + "，退款预计2~5个工作日到账。", "0", hosVisit.getPatientUserId(), hosVisit.getPatientUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    public void saveLog(HosVisitLog hosVisitLog) {
        hosVisitLogMapper.insert(hosVisitLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HosVisit createOrder(HosVisitCreateRequest request) {
        String patientUserId = AuthUtil.getLoginUserId();
        PatientUser patientUser = patientUserMapper.selectById(patientUserId);
        if (StringUtils.isBlank(patientUser.getMobile())) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_MOBILE_NOT_BOUND);
        }

        String doctorId = request.getDoctorId();
        // 旧代码 doctorDetails 返回 DoctorUserVoV2；ddd 拆为 DoctorUserDetailsDto
        com.aihoo.domain.doctor.dto.DoctorUserDetailsDto doctorUser = doctorUserService.doctorDetails(doctorId);
        if (doctorUser == null) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_DOCTOR_NOT_FOUND);
        }

        if (!"1".equals(doctorUser.getStatus())) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_DOCTOR_ACCOUNT_ERROR);
        }
        if (doctorUser.getIsImg() == 0) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_DOCTOR_IS_NOT_IMG);
        }
        if (doctorUser.getIsDisturb() == 1 && !isNotInDisturbFreeRange(doctorUser.getNoDisturbTime())) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_DOCTOR_IS_DISTURB);
        }
        if (countHostVisitByDoctor(doctorId) > doctorUser.getUpperLimit()) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_DOCTOR_OVERSTEP_LIMIT);
        }
        Integer price = doctorUser.getImgPrice();
        if (!Objects.equals(price, request.getPrice())) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_DOCTOR_PRICE_CHANGE);
        }

        HosVisit hosVisit = new HosVisit();
        hosVisit.setPatientUserId(patientUserId);
        hosVisit.setMobile(patientUser.getMobile());
        hosVisit.setTotalPrice(String.valueOf(price));
        hosVisit.setOrderNum("V" + OrderNoUtil.getOrderNo());
        hosVisit.setDoctorUserId(doctorUser.getId());
        hosVisit.setStatus("WAIT");
        baseMapper.insert(hosVisit);

        Order order = new Order();
        order.setOrderType("VISIT");
        order.setPatientUserId(patientUserId);
        order.setOrderNum(hosVisit.getOrderNum());
        order.setOtherId(hosVisit.getId());
        order.setTotalPrice(hosVisit.getTotalPrice());
        orderMapper.insert(order);
        try {
            ImCreateGroupRequestDto imCreateGroupRequest = new ImCreateGroupRequestDto();
            imCreateGroupRequest.setType("Private");
            imCreateGroupRequest.setName("GROUP_" + hosVisit.getOrderNum());
            imCreateGroupRequest.setGroupId("GROUP_" + hosVisit.getOrderNum());
            imCreateGroupRequest.setOwnerAccount("PATIENT_" + hosVisit.getPatientUserId());
            imCreateGroupRequest.setMaxMemberNum(50);

            List<ImCreateGroupRequestDto.MemberItem> memberItems = Lists.newArrayList();
            ImCreateGroupRequestDto.MemberItem memberItem = new ImCreateGroupRequestDto.MemberItem();
            memberItem.setMemberAccount("DOCTOR_" + hosVisit.getDoctorUserId());
            memberItems.add(memberItem);

            imCreateGroupRequest.setMemberList(memberItems);
            log.info("创建群聊,请求参数:{}", JSONObject.toJSONString(imCreateGroupRequest));
            ImCreateGroupRespVo resp = tencentImGroupUtil.createGroup(imCreateGroupRequest);
            log.info("创建群聊,请求结果:{}", JSONObject.toJSONString(resp));
            if (resp.isSuccess()) {
                ImGroup imGroup = new ImGroup();
                imGroup.setGroupId(resp.getGroupId());
                imGroup.setGroupType(imCreateGroupRequest.getType());
                imGroup.setGroupName(imCreateGroupRequest.getName());
                imGroup.setGroupOwnerAccount(imCreateGroupRequest.getOwnerAccount());
                imGroupService.save(imGroup);

                List<ImGroupMember> members = Lists.newArrayList();
                ImGroupMember doctorMember = new ImGroupMember();
                doctorMember.setMemberIdentity("Member");
                doctorMember.setImGroupId(imCreateGroupRequest.getGroupId());
                doctorMember.setMemberType(2);
                doctorMember.setMemberId(Long.valueOf(hosVisit.getDoctorUserId()));
                members.add(doctorMember);

                ImGroupMember patientMember = new ImGroupMember();
                patientMember.setMemberIdentity("Owner");
                patientMember.setImGroupId(imCreateGroupRequest.getGroupId());
                patientMember.setMemberType(1);
                patientMember.setMemberId(Long.valueOf(hosVisit.getPatientUserId()));
                members.add(patientMember);

                imGroupMemberService.saveBatch(members);
            }
            hosVisit.setImGroupId(resp.getGroupId());
            hosVisitMapper.updateById(hosVisit);
        } catch (Exception e) {
            log.info("创建群聊失败:", e);
        }

        return hosVisit;
    }

    @Override
    public HosVisit latestHosVisit(String hosSickId, String doctorId) {
        LambdaQueryWrapper<HosVisit> hosVisitQueryWrapper = new LambdaQueryWrapper<HosVisit>()
                .eq(HosVisit::getHosSickId, hosSickId)
                .eq(HosVisit::getDoctorUserId, doctorId)
                .orderByDesc(HosVisit::getCreateTime)
                .last("limit 1");
        return hosVisitMapper.selectOne(hosVisitQueryWrapper);
    }

    @Override
    public void hosVisitPay(String id) {
        String patientUserId = AuthUtil.getLoginUserId();
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        if (null == hosVisit) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_HOS_VISIT_NOT_FOUND);
        }

        hosVisitMapper.update(new LambdaUpdateWrapper<HosVisit>()
                .eq(HosVisit::getId, id)
                .set(HosVisit::getStatus, "UNSUBMITTED")
                .set(HosVisit::getPayTime, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        HosVisitLog hosVisitLog = new HosVisitLog();
        hosVisitLog.setHosVisitId(hosVisit.getId());
        hosVisitLog.setPatientUserId(patientUserId);
        hosVisitLog.setType("PATIENT");
        hosVisitLog.setDoctorUserId(hosVisit.getDoctorUserId());
        hosVisitLog.setStatus("UNSUBMITTED");
        hosVisitLog.setRemark("患者支付在线问诊订单");
        saveLog(hosVisitLog);
    }

    @Override
    public void addHosSick(String id, String hosSickId) {
        if (StringUtils.isBlank(hosSickId)) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_HOS_SICK_NOT_FOUND);
        }
        HosSick hosSick = hosSickMapper.selectById(hosSickId);
        if (null == hosSick) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_HOS_SICK_NOT_FOUND);
        }
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        if (null == hosVisit) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_HOS_VISIT_NOT_FOUND);
        }
        List<HosVisit> unendedVisit = baseMapper.selectList(new LambdaQueryWrapper<HosVisit>()
                .eq(HosVisit::getDoctorUserId, hosVisit.getDoctorUserId())
                .eq(HosVisit::getPatientUserId, hosVisit.getPatientUserId())
                .eq(HosVisit::getHosSickId, hosSickId)
                .in(HosVisit::getStatus, List.of("UNSUBMITTED", "SUBMITTED", "STARTED"))
        );
        if (CollectionUtils.isNotEmpty(unendedVisit)) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_HOS_VISIT_PAY_STATUS);
        }

        hosVisitMapper.update(new LambdaUpdateWrapper<HosVisit>()
                .eq(HosVisit::getId, id)
                .set(HosVisit::getHosSickId, hosSick.getId())
                .set(HosVisit::getName, hosSick.getName())
                .set(HosVisit::getIdCard, hosSick.getIdCard())
                .set(HosVisit::getSex, hosSick.getSex())
                .set(HosVisit::getAge, hosSick.getAge()));

        try {
            List<DoctorDirectory> dirs = doctorDirectoryService.list(new LambdaQueryWrapper<DoctorDirectory>()
                    .eq(DoctorDirectory::getSickId, hosSick.getId())
                    .eq(DoctorDirectory::getDoctorId, hosVisit.getDoctorUserId())
                    .eq(DoctorDirectory::getPatientUserId, hosSick.getPatientUserId()));
            if (CollectionUtils.isEmpty(dirs)) {
                DoctorDirectory doctorDirectory = new DoctorDirectory();
                doctorDirectory.setSource(2);
                doctorDirectory.setSickName(hosSick.getName());
                doctorDirectory.setSickId(Long.valueOf(hosSick.getId()));
                doctorDirectory.setDoctorId(Long.valueOf(hosVisit.getDoctorUserId()));
                doctorDirectory.setPatientUserId(Long.valueOf(hosSick.getPatientUserId()));
                doctorDirectoryService.save(doctorDirectory);
            } else {
                log.info("通讯录重复,订单ID:{}", id);
            }
        } catch (Exception e) {
            log.info("新增通讯录异常:", e);
        }
    }

    @Override
    public void submitInfo(String id) {
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        if (null == hosVisit) {
            throw new BizException(com.aihoo.common.BizResultCode.PATIENT_HOS_VISIT_NOT_FOUND);
        }

        hosVisitMapper.update(new LambdaUpdateWrapper<HosVisit>()
                .eq(HosVisit::getId, id)
                .set(HosVisit::getStatus, "SUBMITTED")
                .set(HosVisit::getInfoSubmitTime, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        HosVisitLog hosVisitLog = new HosVisitLog();
        hosVisitLog.setHosVisitId(hosVisit.getId());
        hosVisitLog.setPatientUserId(AuthUtil.getLoginUserId());
        hosVisitLog.setType("PATIENT");
        hosVisitLog.setDoctorUserId(hosVisit.getDoctorUserId());
        hosVisitLog.setStatus("SUBMITTED");
        hosVisitLog.setRemark("患者添加问诊资料");
        saveLog(hosVisitLog);

        sendDoctorWelcomeMessage(hosVisit);
    }

    private void sendDoctorWelcomeMessage(HosVisit hosVisit) {
        log.info("开始发送医生欢迎语, doctorUserId={}, hosVisitId={}", hosVisit.getDoctorUserId(), hosVisit.getId());
        DoctorWelcomeMessageSet welcomeMessageSet = doctorWelcomeMessageSetService.getOne(
                new LambdaQueryWrapper<DoctorWelcomeMessageSet>()
                        .eq(DoctorWelcomeMessageSet::getDoctorUserId, hosVisit.getDoctorUserId()));

        log.info("查询欢迎语设置结果: {}", welcomeMessageSet);
        if (welcomeMessageSet != null) {
            log.info("isAuto={}, welcomeMessage={}", welcomeMessageSet.getIsAuto(), welcomeMessageSet.getWelcomeMessage());
        }

        String welcomeMessage = null;
        if (welcomeMessageSet == null) {
            welcomeMessage = "欢迎找我在线复诊，认真填写主诉和问诊单有助于我了解您的病情，我会尽快阅读您的资料，请稍等。";
        } else if (welcomeMessageSet.getIsAuto() != null && welcomeMessageSet.getIsAuto() == 1) {
            welcomeMessage = StringUtils.isNotEmpty(welcomeMessageSet.getWelcomeMessage())
                    ? welcomeMessageSet.getWelcomeMessage()
                    : "欢迎找我在线复诊，认真填写主诉和问诊单有助于我了解您的病情，我会尽快阅读您的资料，请稍等。";
        }

        if (StringUtils.isEmpty(welcomeMessage)) {
            return;
        }

        try {
            String imGroupId = hosVisit.getImGroupId();
            if (StringUtils.isEmpty(imGroupId)) {
                imGroupId = "GROUP_" + hosVisit.getOrderNum();
            }

            ImSendGroupMsgRequestDto request = new ImSendGroupMsgRequestDto();
            request.setGroupId(imGroupId);
            request.setFromAccount("DOCTOR_" + hosVisit.getDoctorUserId());
            request.setToAccount("PATIENT_" + hosVisit.getPatientUserId());
            request.setVisitNo(hosVisit.getOrderNum());
            request.setRandom(System.currentTimeMillis());

            ImSendGroupMsgRequestDto.MessageBody body = new ImSendGroupMsgRequestDto.MessageBody();
            body.setMsgType("TIMTextElem");
            ImSendGroupMsgRequestDto.MsgParam msgParam = new ImSendGroupMsgRequestDto.MsgParam();
            msgParam.setText(welcomeMessage);
            body.setMsgContent(msgParam);
            request.setMsgBody(Lists.newArrayList(body));

            imGroupService.sendMsg(request);
            log.info("发送医生欢迎语成功, doctorUserId={}, visitNo={}", hosVisit.getDoctorUserId(), hosVisit.getOrderNum());
        } catch (Exception e) {
            log.error("发送医生欢迎语失败, doctorUserId={}, visitNo={}", hosVisit.getDoctorUserId(), hosVisit.getOrderNum(), e);
        }
    }

    private boolean isNotInDisturbFreeRange(String disturbFreeRanges) {
        if (disturbFreeRanges == null || disturbFreeRanges.trim().isEmpty()) {
            return true;
        }

        LocalTime now = LocalTime.now();

        String[] ranges = disturbFreeRanges.split(",");
        for (String range : ranges) {
            range = range.trim();
            if (range.isEmpty()) continue;

            String[] parts = range.split("-");
            if (parts.length != 3) {
                continue;
            }

            LocalTime start = LocalTime.parse(parts[0], DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime end = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm"));
            boolean crossDay = "1".equals(parts[2]);

            if (isInTimeRange(now, start, end, crossDay)) {
                return false;
            }
        }

        return true;
    }

    private boolean isInTimeRange(LocalTime now, LocalTime start, LocalTime end, boolean crossDay) {
        if (!crossDay) {
            return !now.isBefore(start) && !now.isAfter(end);
        } else {
            return !now.isBefore(start) || !now.isAfter(end);
        }
    }

    @Override
    public long countHostVisitByDoctor(String doctorId) {
        return hosVisitMapper.selectCount(new LambdaQueryWrapper<HosVisit>()
                .eq(HosVisit::getDoctorUserId, doctorId));
    }

    @Override
    public Long countByDoctorUserId(String doctorUserId) {
        return hosVisitMapper.selectCount(new LambdaQueryWrapper<HosVisit>()
                .eq(HosVisit::getDoctorUserId, doctorUserId));
    }

    @Override
    public long countHosVisitByPatientUserId(String patientUserId) {
        return hosVisitMapper.selectCount(new LambdaQueryWrapper<HosVisit>()
                .eq(HosVisit::getPatientUserId, patientUserId));
    }

    @Override
    public List<HosVisit> listVisitsByHosSickId(String hosSickId) {
        QueryWrapper<HosVisit> visitWrapper = new QueryWrapper<>();
        visitWrapper.eq("hos_sick_id", hosSickId);
        visitWrapper.orderByDesc("create_time");
        return hosVisitMapper.selectList(visitWrapper);
    }

    @Override
    public List<String> listSickIdsByDoctorUserId(String doctorId) {
        if (StringUtils.isEmpty(doctorId)) {
            return List.of();
        }
        List<HosVisit> visitList = hosVisitMapper.selectList(new LambdaQueryWrapper<HosVisit>()
                .select(HosVisit::getHosSickId)
                .eq(HosVisit::getDoctorUserId, doctorId));
        return visitList.stream().map(HosVisit::getHosSickId).distinct().toList();
    }

    @Override
    public void addHealthInfo(HosVisitInfoRequest request) {
        hosVisitMapper.update(new LambdaUpdateWrapper<HosVisit>()
                .eq(HosVisit::getId, request.getHosVisitId())
                .set(HosVisit::getHealthInfo, request.getHealthInfo()));
    }

    @Override
    public void addBaseInfo(HosVisitInfoRequest request) {
        HosSickHealthRecords records = hosSickHealthRecordsService
                .getOne(new LambdaQueryWrapper<HosSickHealthRecords>()
                        .eq(HosSickHealthRecords::getHosSickId, request.getBaseInfo().getHosSickId()));
        if (records == null) {
            records = new HosSickHealthRecords();
        }
        HosVisitBaseInfoDTO baseInfo = request.getBaseInfo();
        records.setArea(baseInfo.getArea());
        records.setAreaName(baseInfo.getAreaName());
        records.setHeight(baseInfo.getHeight());
        records.setHosSickId(request.getBaseInfo().getHosSickId());
        records.setWeight(baseInfo.getWeight());
        records.setAllergyHistory(baseInfo.getAllergyHistory());
        records.setPastHistory(baseInfo.getMedicalHistory());
        records.setTongueImages(StringUtils.join(baseInfo.getTongueImages(), ","));
        records.setFaceImages(StringUtils.join(baseInfo.getFaceImages(), ","));
        records.setMedicalRecordImages(StringUtils.join(baseInfo.getMedicalRecordImages(), ","));
        hosSickHealthRecordsService.saveOrUpdate(records);

        if (StringUtils.isNotBlank(request.getHosVisitId())) {
            hosVisitMapper.update(null, new LambdaUpdateWrapper<HosVisit>()
                    .eq(HosVisit::getId, request.getHosVisitId())
                    .set(HosVisit::getBaseInfo, "1")
                    .set(StringUtils.isNotBlank(baseInfo.getDesc()), HosVisit::getContent, baseInfo.getDesc()));
        }
    }

    @Override
    public void updateBaseInfo(HosVisitInfoRequest request) {
        HosSickHealthRecords records = hosSickHealthRecordsService
                .getOne(new LambdaQueryWrapper<HosSickHealthRecords>()
                        .eq(HosSickHealthRecords::getHosSickId, request.getBaseInfo().getHosSickId()));
        if (records == null) {
            return;
        }
        HosVisitBaseInfoDTO baseInfo = request.getBaseInfo();
        hosSickHealthRecordsService.update(new LambdaUpdateWrapper<HosSickHealthRecords>()
                .set(StringUtils.isNotEmpty(baseInfo.getArea()), HosSickHealthRecords::getArea, baseInfo.getArea())
                .set(StringUtils.isNotEmpty(baseInfo.getAreaName()), HosSickHealthRecords::getAreaName, baseInfo.getAreaName())
                .set(StringUtils.isNotEmpty(baseInfo.getHeight()), HosSickHealthRecords::getHeight, baseInfo.getHeight())
                .set(StringUtils.isNotEmpty(baseInfo.getWeight()), HosSickHealthRecords::getWeight, baseInfo.getWeight())
                .set(StringUtils.isNotEmpty(baseInfo.getAllergyHistory()), HosSickHealthRecords::getAllergyHistory, baseInfo.getAllergyHistory())
                .set(StringUtils.isNotEmpty(baseInfo.getMedicalHistory()), HosSickHealthRecords::getPastHistory, baseInfo.getMedicalHistory())
                .set(CollectionUtils.isNotEmpty(baseInfo.getTongueImages()), HosSickHealthRecords::getTongueImages, StringUtils.join(baseInfo.getTongueImages(), ","))
                .set(CollectionUtils.isNotEmpty(baseInfo.getFaceImages()), HosSickHealthRecords::getFaceImages, StringUtils.join(baseInfo.getFaceImages(), ","))
                .set(CollectionUtils.isNotEmpty(baseInfo.getMedicalRecordImages()), HosSickHealthRecords::getMedicalRecordImages, StringUtils.join(baseInfo.getMedicalRecordImages(), ","))
                .eq(HosSickHealthRecords::getId, records.getId()));

        if (StringUtils.isNotBlank(request.getHosVisitId())) {
            hosVisitMapper.update(null, new LambdaUpdateWrapper<HosVisit>()
                    .eq(HosVisit::getId, request.getHosVisitId())
                    .set(HosVisit::getBaseInfo, "1"));
        }
    }

    @Override
    public HosVisitHealthInfoVo getHealthInfo(String hosVisitId) {
        HosVisitHealthInfoVo healthInfoVo = new HosVisitHealthInfoVo();
        healthInfoVo.setHosVisitId(hosVisitId);
        HosVisit hosVisit = hosVisitMapper.selectById(hosVisitId);
        if (StringUtil.isNotBlank(hosVisit.getHealthInfo())) {
            healthInfoVo.setHealthInfo(hosVisit.getHealthInfo());
        }
        healthInfoVo.setCreateTime(hosVisit.getCreateTime());
        return healthInfoVo;
    }

    @Override
    public HosVisitBaseInfoVo getBaseInfo(String hosVisitId) {
        HosVisitBaseInfoVo baseInfoVo = new HosVisitBaseInfoVo();
        baseInfoVo.setHosVisitId(hosVisitId);
        HosVisit hosVisit = hosVisitMapper.selectById(hosVisitId);
        HosVisitBaseInfoDTO baseInfo = new HosVisitBaseInfoDTO();
        baseInfoVo.setBaseInfo(baseInfo);

        baseInfo.setDesc(hosVisit.getContent());

        HosSickHealthRecords healthRecords = hosSickHealthRecordsService.getOne(new LambdaQueryWrapper<HosSickHealthRecords>()
                .eq(HosSickHealthRecords::getHosSickId, hosVisit.getHosSickId()));
        if (healthRecords != null) {
            baseInfo.setArea(healthRecords.getArea());
            baseInfo.setAreaName(healthRecords.getAreaName());
            baseInfo.setHeight(healthRecords.getHeight());
            baseInfo.setWeight(healthRecords.getWeight());
            baseInfo.setAllergyHistory(healthRecords.getAllergyHistory());
            baseInfo.setMedicalHistory(healthRecords.getPastHistory());
            if (StringUtils.isNotEmpty(healthRecords.getFaceImages()))
                baseInfo.setFaceImages(Lists.newArrayList(healthRecords.getFaceImages().split(",")));
            if (StringUtils.isNotEmpty(healthRecords.getTongueImages()))
                baseInfo.setTongueImages(Lists.newArrayList(healthRecords.getTongueImages().split(",")));
            if (StringUtils.isNotEmpty(healthRecords.getMedicalRecordImages()))
                baseInfo.setMedicalRecordImages(Lists.newArrayList(healthRecords.getMedicalRecordImages().split(",")));
        }
        baseInfoVo.setCreateTime(hosVisit.getCreateTime());
        return baseInfoVo;
    }

    private String getAvatarPath(String sex, String ageStr) {
        return AvatarUtil.getAvatarPath(sex, ageStr);
    }

    @Override
    public HosOrderDto visitData(String id) {
        HosOrderDto hosOrder = new HosOrderDto();
        HosVisit hosVisit;
        if (id != null && id.startsWith("V")) {
            hosVisit = hosVisitMapper.selectOne(new LambdaQueryWrapper<HosVisit>()
                    .eq(HosVisit::getOrderNum, id));
        } else {
            hosVisit = hosVisitMapper.selectById(id);
        }
        if (hosVisit == null) {
            return hosOrder;
        }
        if (StringUtils.isNotEmpty(hosVisit.getHosSickId())) {
            HosSickHealthRecords record = hosSickHealthRecordsService.getOne(new LambdaQueryWrapper<HosSickHealthRecords>()
                    .eq(HosSickHealthRecords::getHosSickId, hosVisit.getHosSickId())
                    .last("limit 1"));
            if (record != null) {
                hosOrder.setAreaCode(record.getArea());
                hosOrder.setAreaName(record.getAreaName());
            }
            hosOrder.setHosSickId(hosVisit.getHosSickId());
        }

        hosOrder.setDoctorId(hosVisit.getDoctorUserId());
        hosOrder.setSex(hosVisit.getSex());
        hosOrder.setAge(hosVisit.getAge());
        hosOrder.setName(hosVisit.getName());
        hosOrder.setPayTime(hosVisit.getPayTime());
        hosOrder.setContent(hosVisit.getContent());
        hosOrder.setOrderNum(hosVisit.getOrderNum());
        hosOrder.setCreateTime(hosVisit.getCreateTime());
        hosOrder.setPatientId(hosVisit.getPatientUserId());
        hosOrder.setType(hosVisit.getType());
        hosOrder.setFirstVisit(hosVisit.getFirstVisit());
        hosOrder.setDoctorAdvice(hosVisit.getDoctorAdvice());
        hosOrder.setOrderId(hosVisit.getId());
        if (StringUtils.isNotEmpty(hosVisit.getPatientUserId())) {
            PatientUser patientUser = patientUserMapper.selectById(hosVisit.getPatientUserId());
            if (patientUser != null) {
                hosOrder.setHeadImg(patientUser.getHeadImg());
            }
        }
        List<HosVisitImg> hosVisitImgs = hosVisitImgMapper.selectByHosVisitId(id);
        List<Object> imgList = new ArrayList<>();
        for (HosVisitImg hosVisitImg : hosVisitImgs) {
            imgList.add(hosVisitImg.getImg());
        }
        hosOrder.setImgList(imgList);

        String status = hosVisit.getStatus();
        hosOrder.setStatus(status);
        hosOrder.setStatusName("在线复诊" + status);

        if ("END".equals(status)) {
            hosOrder.setEndTime(hosVisit.getEndTime());
            hosOrder.setFiveStar(StringUtil.isBlank(hosVisit.getFiveStar()) ? "" : hosVisit.getFiveStar());
        }
        // countDown：老 doctor-api 用 TBase 取 VISIT_TIMES / DOCTOR_VISIT_TIMES 算倒计时；DDD 阶段简化为空，由 controller 按需补。
        hosOrder.makeVisitbtnJson();
        return hosOrder;
    }
}