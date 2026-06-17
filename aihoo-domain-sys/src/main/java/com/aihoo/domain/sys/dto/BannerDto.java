package com.aihoo.domain.sys.dto;

import lombok.Data;

@Data
public class BannerDto {
    private String id;
    private String createTime;
    private String updateTime;
    private String createUserId;
    private String type;
    private String otherId;
    private String title;
    private String img;
    private String content;
    private String index;
    private String isDelete;
    private String bannerType;
    private String videoUrl;

    private String bannerTypeName;
    private String typeName;
}
