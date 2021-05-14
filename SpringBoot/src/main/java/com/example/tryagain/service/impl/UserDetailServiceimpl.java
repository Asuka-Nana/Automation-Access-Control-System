package com.example.tryagain.service.impl;

import com.example.tryagain.pojo.UserDetail;
import com.example.tryagain.mapper.UserMapper;
import com.example.tryagain.pojo.User;
import com.example.tryagain.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service("UserDetailService")
public class UserDetailServiceimpl implements UserDetailService {
    @Autowired
    UserMapper userMapper;


    @Override
    public UserDetail getdetail (String username){
        InputStream inputStream = null;
        byte[] buffer = null;
        //读取图片字节数组
        try {
            inputStream = new FileInputStream("D:\\data\\"+username+".jpg");
            int count = 0;
            while (count == 0) {
                count = inputStream.available();
            }
            buffer = new byte[count];
            inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String src = Base64.getEncoder().encodeToString(buffer);
        User user = userMapper.findpwdbyname(username);
        UserDetail userDetail = new UserDetail (user.getUsername() , user.getName(),user.getState(),user.getDepartment(),user.getEmail(),user.getTelephone(),user.getLocation(),user.getDiscription(),src);
        return userDetail;
    }
}
