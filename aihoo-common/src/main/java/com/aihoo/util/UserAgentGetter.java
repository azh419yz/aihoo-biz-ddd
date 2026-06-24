package com.aihoo.util;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UserAgentGetter {
    private String userAgentString;
    private HttpServletRequest request;

    public UserAgentGetter(HttpServletRequest request) {
        this.request = request;
        userAgentString = request.getHeader("osName");
    }

    
    public String getOS() {
        if (null == userAgentString) {
            return "未知设备";
        }
        return userAgentString;
    }

    
    public String getDevice() {
        if (null == userAgentString) {
            return "未知";
        }
        String ua = userAgentString.toLowerCase();
        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ios")) {
            return "iOS";
        }
        if (ua.contains("android")) {
            return "Android";
        }
        if (ua.contains("windows")) {
            return "Windows";
        }
        if (ua.contains("mac")) {
            return "Mac";
        }
        return "未知";
    }

    
    public String getBrowser() {
        if (null == userAgentString) {
            return "未知";
        }
        String ua = userAgentString.toLowerCase();
        if (ua.contains("micromessenger")) {
            return "微信";
        }
        if (ua.contains("edg/")) {
            return "Edge";
        }
        if (ua.contains("chrome/")) {
            return "Chrome";
        }
        if (ua.contains("safari/")) {
            return "Safari";
        }
        if (ua.contains("firefox/")) {
            return "Firefox";
        }
        return "未知";
    }

    
    public String getIpAddr() {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (isBlankIp(ip))
                ip = request.getHeader("Proxy-Client-IP");
            if (isBlankIp(ip))
                ip = request.getHeader("WL-Proxy-Client-IP");
            if (isBlankIp(ip))
                ip = request.getHeader("HTTP_CLIENT_IP");
            if (isBlankIp(ip))
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (isBlankIp(ip))
                ip = request.getRemoteAddr();

            if (!isBlankIp(ip) && ip.length() > 15)
                ip = ip.split(",")[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        }
        return ip;
    }

    private boolean isBlankIp(String str) {
        return str == null || str.trim().isEmpty() || "unknown".equalsIgnoreCase(str);
    }
}
