package com.example.tryagain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetail {
    private String username;
    private String name;
    private Integer state;
    private Integer department;
    private String email;
    private String telephone;
    private String location;
    private String discription;
    private String img;

}
