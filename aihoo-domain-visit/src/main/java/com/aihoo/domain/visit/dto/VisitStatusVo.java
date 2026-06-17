package com.aihoo.domain.visit.dto;

import lombok.Data;

import java.util.List;

@Data
public class VisitStatusVo {
    private String now;
    private List<String> before;
    private List<String> after;
}