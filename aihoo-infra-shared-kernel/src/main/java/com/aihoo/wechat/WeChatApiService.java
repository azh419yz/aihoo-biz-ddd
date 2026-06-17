package com.aihoo.wechat;

import com.alibaba.fastjson2.JSONObject;

/**
 * 微信API服务
 */
public interface WeChatApiService {

	/**
	 * 获取Access Token 注意：生产环境建议使用Redis缓存，避免频繁调用
	 */
	String getAccessToken();

	/**
	 * 生成小程序码
	 *
	 * @param accessToken 访问令牌
	 * @param requestData 请求参数
	 * @return 小程序码的Base64编码
	 */
	String generateWxaCode(String accessToken, JSONObject requestData);
}
