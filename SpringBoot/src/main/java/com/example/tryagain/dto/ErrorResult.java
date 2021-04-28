package com.example.tryagain.dto;

import lombok.Data;

@Data
public class ErrorResult {
    private String errormsg;
    private Integer code;
}
