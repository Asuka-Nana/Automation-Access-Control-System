package com.example.tryagain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDetail {
    public Integer cid;
    public Integer nid;
    public String username;
    public String time;
    public String content;
}
