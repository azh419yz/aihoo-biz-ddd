package com.aihoo.api.doctor.request;

import lombok.Data;

@Data
public class WxaCodeRequest {

    
    private String page;

    
    private String scene;

    
    private Integer width = 430;

    
    private Boolean autoColor = true;

    
    private Object lineColor;

    
    private Boolean isHyaline = false;
}
