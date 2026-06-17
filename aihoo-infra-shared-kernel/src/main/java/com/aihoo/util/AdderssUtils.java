package com.aihoo.util;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class AdderssUtils {
    static Log log = LogFactory.get();

    public static String getCityNameByTaoBaoAPI(String ip) {
        String cityName = "";
        try {
            String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.wait(1000L);
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                try {
                    JSONObject jsonResult = JSON.parseObject(strResult);
                    JSONObject dataJson = jsonResult.getJSONObject("data");
                    cityName = dataJson.getString("city");
                    if (null == cityName || "".equals(cityName)) {
                        cityName = "";
                    }
                    System.out.println(JSON.toJSONString(jsonResult, JSONWriter.Feature.PrettyFormat));
                } catch (Exception e) {
                    return null;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            log.error("***************获取城市异常*****************");
            return null;
        }
        return cityName;
    }
}
