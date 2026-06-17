package com.aihoo.util;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取客户端设备信息
 * Created by wangfan on 2017-6-10 上午10:10
 */
public class UserAgentGetter {
    private String userAgentString;
    private HttpServletRequest request;

    public UserAgentGetter(HttpServletRequest request) {
        this.request = request;
        userAgentString = request.getHeader("osName");
    }

    /**
     * 获取操作系统
     */
    public String getOS() {
        if (null == userAgentString) {
            return "未知设备";
        }
        return userAgentString;
    }

    /**
     * 获取设备类型（基于 User-Agent 简单判断）
     */
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

    /**
     * 获取浏览器
     */
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

    /**
     * 获取ip地址
     */
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
            // 使用代理，则获取第一个IP地址
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
