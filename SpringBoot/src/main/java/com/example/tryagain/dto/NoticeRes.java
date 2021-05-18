package com.example.tryagain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeRes {
    Integer id;
    String username;
    String time;
    String content;
    String img;
}
