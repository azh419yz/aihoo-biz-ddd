package com.aihoo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alicloud")
public class AliCloudProperties {
    private String ak;
    private String sk;
    private Auth auth;
    private Sms sms;
    private Oss oss;

    public String getAk() { return ak; }
    public void setAk(String ak) { this.ak = ak; }
    public String getSk() { return sk; }
    public void setSk(String sk) { this.sk = sk; }
    public Auth getAuth() { return auth; }
    public void setAuth(Auth auth) { this.auth = auth; }
    public Sms getSms() { return sms; }
    public void setSms(Sms sms) { this.sms = sms; }
    public Oss getOss() { return oss; }
    public void setOss(Oss oss) { this.oss = oss; }

    public static class Auth {
        private String endpoint;
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    }

    public static class Sms {
        private String endpoint;
        private String signName;
        private String templateCode;
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public String getSignName() { return signName; }
        public void setSignName(String signName) { this.signName = signName; }
        public String getTemplateCode() { return templateCode; }
        public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }
    }

    public static class Oss {
        private String endpoint;
        private String bucketName;
        private String imgHeadUrl;
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public String getBucketName() { return bucketName; }
        public void setBucketName(String bucketName) { this.bucketName = bucketName; }
        public String getImgHeadUrl() { return imgHeadUrl; }
        public void setImgHeadUrl(String imgHeadUrl) { this.imgHeadUrl = imgHeadUrl; }
    }
}