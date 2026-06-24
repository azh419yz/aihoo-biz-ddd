package com.aihoo.wechat;

import com.alibaba.fastjson2.JSONObject;

public interface WeChatApiService {

	
	String getAccessToken();

	
	String generateWxaCode(String accessToken, JSONObject requestData);
}
