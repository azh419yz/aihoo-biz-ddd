package com.aihoo.domain.im.util;

import com.aihoo.domain.im.constant.ImRequestUrlConstant;
import com.aihoo.domain.im.dto.ImAddGroupMemberRequestDto;
import com.aihoo.domain.im.dto.ImAddGroupMemberRespVo;
import com.aihoo.domain.im.dto.ImCreateGroupRequestDto;
import com.aihoo.domain.im.dto.ImCreateGroupRespVo;
import com.aihoo.domain.im.dto.ImDeleteGroupMemberRequestDto;
import com.aihoo.domain.im.dto.ImGetGroupMemberInfoRequestDto;
import com.aihoo.domain.im.dto.ImGetGroupMemberInfoRespVo;
import com.aihoo.domain.im.dto.ImRespVo;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.dto.ImSendGroupMsgRespVo;
import com.aihoo.properties.TencentProperties;
import com.aihoo.util.ImUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class TencentImGroupUtil {

    private final TencentProperties tencentProperties;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private String generateUserSig() {
        return ImUtils.genUserSig(tencentProperties.getAdminidentifier(), null,
                tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
    }

    private <T extends ImRespVo> T executeRequest(String command, Object reqParams, Class<T> responseClass) {
        T result;
        try {
            String url = ImRequestUrlConstant.GROUP_URL + command +
                    "?sdkappid=" + tencentProperties.getSdkappid() +
                    "&identifier=" + tencentProperties.getAdminidentifier() +
                    "&usersig=" + generateUserSig() +
                    "&random=" + (int) (Math.random() * 100000000) +
                    "&contenttype=json";

            String jsonBody = JSON.toJSONString(reqParams);
            RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonBody);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody;
                if (response.body() != null) {
                    responseBody = response.body().string();
                } else {
                    responseBody = "{}";
                }

                if (!response.isSuccessful()) {
                    result = responseClass.getDeclaredConstructor().newInstance();
                    fillErrorResponse(result, response.code(), "网络异常:" + response.message() + "。 信息: " + responseBody);
                    return result;
                }

                try {
                    result = JSON.parseObject(responseBody, responseClass);
                    if (result == null) {
                        result = responseClass.getDeclaredConstructor().newInstance();
                        fillErrorResponse(result, -2, "解析json异常或者请求结果对象为空");
                    }
                } catch (JSONException e) {
                    result = responseClass.getDeclaredConstructor().newInstance();
                    fillErrorResponse(result, -3, "json转换异常 " + e.getMessage());
                }
                return result;
            }
        } catch (IOException e) {
            try {
                result = responseClass.getDeclaredConstructor().newInstance();
                fillErrorResponse(result, -4, "http网络异常: " + e.getMessage());
                return result;
            } catch (Exception ex) {
                throw new RuntimeException("实例化对象异常:", ex);
            }
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("反序列化对象异常" + responseClass.getName(), e);
        }
    }

    private void fillErrorResponse(ImRespVo response, int errorCode, String errorInfo) {
        response.setActionStatus("FAIL");
        response.setErrorCode(errorCode);
        response.setErrorInfo(errorInfo);
    }

    public ImCreateGroupRespVo createGroup(ImCreateGroupRequestDto req) {
        return executeRequest(ImRequestUrlConstant.CREATE_GROUP_URL, req, ImCreateGroupRespVo.class);
    }

    public ImAddGroupMemberRespVo addGroupMember(ImAddGroupMemberRequestDto req) {
        return executeRequest(ImRequestUrlConstant.ADD_MEMBER_URL, req, ImAddGroupMemberRespVo.class);
    }

    public ImRespVo deleteGroupMember(ImDeleteGroupMemberRequestDto req) {
        return executeRequest(ImRequestUrlConstant.DELETE_MEMBER_URL, req, ImRespVo.class);
    }

    public ImSendGroupMsgRespVo sendGroupMessage(ImSendGroupMsgRequestDto req) {
        if (req.getRandom() == null) {
            req.setRandom((long) (Math.random() * 100000000));
        }
        return executeRequest(ImRequestUrlConstant.SEND_GROUP_MSG, req, ImSendGroupMsgRespVo.class);
    }

    public ImGetGroupMemberInfoRespVo getGroupMemberInfo(ImGetGroupMemberInfoRequestDto req) {
        if (req.getOffset() == null) {
            req.setOffset(0);
        }
        if (req.getLimit() == null) {
            req.setLimit(500);
        } else if (req.getLimit() > 500) {
            req.setLimit(500);
        }
        return executeRequest(ImRequestUrlConstant.GET_GROUP_MEMBER_INFO, req, ImGetGroupMemberInfoRespVo.class);
    }
}
