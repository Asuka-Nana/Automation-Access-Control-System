package com.example.tryagain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentRes {
    private NoticeRes notice;
    private List<Comment> comments;
    private Integer code;
}
