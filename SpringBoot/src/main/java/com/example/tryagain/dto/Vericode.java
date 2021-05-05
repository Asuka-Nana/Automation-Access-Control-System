package com.example.tryagain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vericode {
    String token;
    String vericode;

    public static Vericode example(String token ,String img){
        return new Vericode(token,img);
    }
}
