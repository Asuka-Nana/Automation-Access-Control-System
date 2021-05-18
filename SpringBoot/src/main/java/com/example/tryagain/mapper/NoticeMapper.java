package com.example.tryagain.mapper;

import com.example.tryagain.pojo.notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
    notice[] getnotice(Integer department);
}
