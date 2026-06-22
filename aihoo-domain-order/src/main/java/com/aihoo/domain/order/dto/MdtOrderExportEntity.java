package com.aihoo.domain.order.dto;

import com.aihoo.domain.sys.excel.ExcelColumn;
import lombok.Data;

import java.io.Serializable;

@Data
public class MdtOrderExportEntity implements Serializable {

    @ExcelColumn(value = "订单ID", col = 1)
    private String orderId;

    @ExcelColumn(value = "处方ID", col = 2)
    private String preId;

    @ExcelColumn(value = "支付时间", col = 3)
    private String payTime;

    @ExcelColumn(value = "患者信息", col = 4)
    private String hosSickMsg;

    @ExcelColumn(value = "药态", col = 5)
    private String medicineStatusCode;

    @ExcelColumn(value = "剂数", col = 6)
    private String doseNumber;

    @ExcelColumn(value = "药费", col = 7)
    private String price;
}