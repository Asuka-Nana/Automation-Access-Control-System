package com.example.tryagain.dto;

import lombok.Data;

@Data
public class AddUser {
    private String username;
    private String name;
    private Integer role;
    private Integer department;
    private String email;
    private String location;
    private String discription;
    private String telephone;
}
