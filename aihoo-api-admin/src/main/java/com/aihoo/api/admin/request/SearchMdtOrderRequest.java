package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "订单查询请求参数")
public class SearchMdtOrderRequest {

    @Schema(description = "患者姓名", example = "里斯")
    private String hosSickName;

    @Schema(description = "收件姓名", example = "张三")
    private String receiveName;

    @Schema(description = "收件手机", example = "18600001111")
    private String receivePhone;

    @Schema(description = "订单ID", example = "1")
    private String orderId;

    @Schema(description = "处方ID", example = "1")
    private String preId;

    @Schema(description = "药态CODE", example = "1")
    private Integer medicineStatus;

    @Schema(description = "药房ID", example = "1")
    private String drugstoreId;

    @Schema(description = "订单状态", example = "1")
    private String status;

    @Schema(description = "支付开始时间", example = "2024-01-01 00:00:00")
    private String payStartTime;

    @Schema(description = "支付结束时间", example = "2024-12-31 23:59:59")
    private String payEndTime;

    @Schema(description = "是否已上传图片", example = "true")
    private Boolean havePic;

    private List<String> statusList;

}