package com.example.tryagain.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String vericode;
    private String token;
}
