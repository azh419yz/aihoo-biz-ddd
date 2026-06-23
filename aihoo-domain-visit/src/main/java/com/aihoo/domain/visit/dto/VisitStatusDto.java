package com.aihoo.domain.visit.dto;

import lombok.Data;

import java.util.List;

@Data
public class VisitStatusDto {
    private String now;
    private List<String> before;
    private List<String> after;
}