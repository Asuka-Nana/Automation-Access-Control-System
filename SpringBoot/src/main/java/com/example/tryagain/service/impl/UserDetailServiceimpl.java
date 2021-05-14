package com.example.tryagain.service.impl;

import com.example.tryagain.dto.UserDetail;
import com.example.tryagain.mapper.UserMapper;
import com.example.tryagain.pojo.User;
import com.example.tryagain.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserDetailService")
public class UserDetailServiceimpl implements UserDetailService {
    @Autowired
    UserMapper userMapper;


    @Override
    public UserDetail getdetail (String username){
        User user = userMapper.findpwdbyname(username);
        UserDetail userDetail = new UserDetail (user.getUsername() , user.getName(),user.getState(),user.getDepartment(),user.getEmail(),user.getTelephone(),user.getLocation(),user.getDiscription());
        return userDetail;
    }
}
