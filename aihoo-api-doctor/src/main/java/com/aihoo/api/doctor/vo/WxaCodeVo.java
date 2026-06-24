package com.aihoo.api.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "返回的二维码 VO")
@Data
public class WxaCodeVo {
	
	@Schema(name = "qrcode", description = "小程序码的Base64编码")
	private String qrcode;

	
	@Schema(name = "contentType", description = "小程序码的图片类型", example = "image/png")
	private String contentType = "image/png";
}
