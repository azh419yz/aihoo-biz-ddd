package com.aihoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "meiqing")
public class MeiqingProperties {
    private String counsel;
    private String accept;
    private String confirmation;
    private String cancel;
    private String docLogin;
    private String upload;
    private String dicomUrl;
}
