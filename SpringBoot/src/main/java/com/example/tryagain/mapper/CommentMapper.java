package com.example.tryagain.mapper;

import com.example.tryagain.dto.CommentDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    CommentDetail[] getcomment (Integer nid);
    void addcomment (String username,String time,String comment,Integer id);
}
