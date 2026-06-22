package com.aihoo.domain.order.dto;

import com.aihoo.domain.sys.excel.ExcelColumn;
import lombok.Data;

import java.io.Serializable;

@Data
public class MdtOrderDrugExportEntity implements Serializable {

    @ExcelColumn(value = "药品ID", col = 1)
    private String drugId;

    @ExcelColumn(value = "药品名", col = 2)
    private String name;

    @ExcelColumn(value = "用量", col = 3)
    private String number;

    @ExcelColumn(value = "单位", col = 4)
    private String unit;
}