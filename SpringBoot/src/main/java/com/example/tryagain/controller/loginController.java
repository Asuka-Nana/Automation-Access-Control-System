package com.example.tryagain.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.example.tryagain.dto.LoginRequest;
import com.example.tryagain.dto.Vericode;
import com.example.tryagain.dto.logRes;
import com.example.tryagain.pojo.Usertoken;
import com.example.tryagain.service.codeService;
import com.example.tryagain.service.loginService;

import com.example.tryagain.util.parsingtoken;
import com.example.tryagain.util.sha;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
//import org.apache.commons.codec.binary.Base64;

@RestController
public class loginController {

    @Autowired
    private loginService service;

    @Autowired
    private codeService codeservice;

    @RequestMapping("/login")
    public logRes loginauth (@RequestBody LoginRequest request){
        String username = request.getUsername();
        String password = request.getPassword();
        String vericode = request.getVericode();
        String token = request.getToken();
        String code = sha.SHA1(vericode);
        if (code.equals(token)) {
            return service.verifypwdbyname(username, password);
        }
        return logRes.generateFailResult(3,"验证码错误");
    }

    @RequestMapping("/code")
    public Vericode getcode(){
        return codeservice.getvericode();
    }


    @RequestMapping("/dis")
    @RequiresPermissions("0")
    public String loginaut (@RequestHeader("Authorization") String token ){
       System.out.println(token);
       String s = token;
        s = s.replace("Bearer","");
        String[] tmp = s.split("\\.");
        System.out.println(tmp.length);
        Base64 decoder = new Base64(true);
        byte[] decodedBytes = decoder.decode(tmp[1]);
        String res = new String(decodedBytes);
        System.out.println(res);
        String user = null;
        try{
            Usertoken usertoken = new ObjectMapper().readValue(res, Usertoken.class);
            user = usertoken.getSub();
        }
        catch( com.fasterxml.jackson.core.JsonProcessingException e){
        }
        System.out.println(user);
        return "Hello world!" + user;
    }
}
