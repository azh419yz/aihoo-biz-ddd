package com.aihoo.api.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "返回的二维码 VO")
@Data
public class WxaCodeVo {
	/**
	 * 小程序码的Base64编码（成功时返回）
	 */
	@Schema(name = "qrcode", description = "小程序码的Base64编码")
	private String qrcode;

	/**
	 * 二维码类型标识
	 */
	@Schema(name = "contentType", description = "小程序码的图片类型", example = "image/png")
	private String contentType = "image/png";
}
