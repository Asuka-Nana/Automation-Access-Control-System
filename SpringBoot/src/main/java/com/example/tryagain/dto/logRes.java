package com.example.tryagain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class logRes {
    private Integer code;
    private String errmsg;
    private Object data;

    public static logRes generateSucessResult(Object data){
        return new logRes(0,null,data);
    }

    public static logRes generateFailResult(Integer code,String msg){
        return new logRes(code,msg,null);
    }
}
