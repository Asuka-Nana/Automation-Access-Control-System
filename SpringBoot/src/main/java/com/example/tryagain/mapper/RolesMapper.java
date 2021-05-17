package com.example.tryagain.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolesMapper {
    Integer[] getPrivilege (Integer role);
}
