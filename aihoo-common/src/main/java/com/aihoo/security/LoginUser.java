package com.aihoo.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    private String name;
    
    private String type;
}
