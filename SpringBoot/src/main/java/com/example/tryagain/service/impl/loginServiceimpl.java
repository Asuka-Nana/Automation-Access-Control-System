package com.example.tryagain.service.impl;

import com.example.tryagain.dto.logRes;
import com.example.tryagain.mapper.UserMapper;
import com.example.tryagain.pojo.User;
import com.example.tryagain.service.loginService;
import com.example.tryagain.util.parsingtoken;
import com.louislivi.fastdep.shirojwt.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class loginServiceimpl implements loginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public logRes verifypwdbyname (String username, String password) {
        if(userMapper.findpwdbyname(username) == null){
            return logRes.generateFailResult(1,"用户不存在");
        }
        else if(password.equals(userMapper.findpwdbyname(username).getPassword())){
            return logRes.generateSucessResult(jwtUtil.sign(username));
        }
        else {
            return logRes.generateFailResult(2,"密码错误");
        }
    }
}
