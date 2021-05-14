package com.example.tryagain.mapper;

import com.example.tryagain.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findpwdbyname (String username);
    void changepwd (String username, String password);
    void changedetail(String username, String name,String email, String telephone, String location, String discription);
}

