package com.example.tryagain.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttendanceMapper {
    String[] getattend(String username,String begin , String end);

}
