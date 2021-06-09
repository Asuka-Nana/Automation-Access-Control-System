package com.example.tryagain.mapper;

import com.example.tryagain.pojo.notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
    notice[] getnotice(Integer department);
    void addnotice (String username, String date, Integer department,String content);
    Integer getdepbyid (Integer id);
    notice getnoticebyid (Integer id);
}
