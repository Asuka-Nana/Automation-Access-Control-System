package com.example.tryagain.mapper;

import com.example.tryagain.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findpwdbyname (String username);
}
