package com.aihoo.util;

import com.aihoo.properties.BaiduProperties;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度地图地理编码/逆编码工具
 * 凭据从 {@link BaiduProperties} 注入，禁止硬编码。
 */
@Component
@RequiredArgsConstructor
public class BaiduMapUtils {

    private final BaiduProperties baiduProperties;

    public Map<String, String> getLatitude(String address) {
        try {
            address = URLEncoder.encode(address, "UTF-8");

            URL resjson = new URL("http://api.map.baidu.com/geocoder?address="
                    + address + "&output=json&key=" + baiduProperties.getAppKey());
            BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream()));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            if (str != null && !str.equals("")) {
                Map<String, String> map = null;
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("},\"precise");
                if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                    String lng = str.substring(lngStart + 5, lngEnd);
                    String lat = str.substring(lngEnd + 7, latEnd);
                    map = new HashMap<String, String>();
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getLocation(String lat, String lng) throws Exception {
        String location = lat + "," + lng;
        URL url = new URL("http://api.map.baidu.com/geocoder?ak=" + baiduProperties.getAppKey()
                + "&callback=renderReverse&location=" + location + "&output=json");
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
        out.flush();
        out.close();
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(l_urlStream, "UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        String str = sb.toString();
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotEmpty(str)) {
            int addressStart = str.indexOf("formatted_address\":");
            int addressEnd = str.indexOf("\",\"business");
            int provinceStart = str.indexOf("province\":");
            int provinceEnd = str.indexOf("\",\"street");
            int cityStart = str.indexOf("city\":");
            int cityEnd = str.indexOf("\",\"direction");
            int districtStart = str.indexOf("district\":");
            int districtEnd = str.indexOf("\",\"province");

            if (addressStart > 0 && addressEnd > 0) {
                String address = str.substring(addressStart + 20, addressEnd);
                String province = str.substring(provinceStart + 11, provinceEnd);
                String city = str.substring(cityStart + 7, cityEnd);
                String district = str.substring(districtStart + 11, districtEnd);
                jsonObject.put("address", address);
                jsonObject.put("province", province);
                jsonObject.put("city", city);
                jsonObject.put("district", district);
                return jsonObject;
            }
        }
        return jsonObject;
    }
}
