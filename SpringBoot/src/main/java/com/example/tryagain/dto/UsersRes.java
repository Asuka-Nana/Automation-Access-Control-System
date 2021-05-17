package com.example.tryagain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UsersRes {
    List<List<String>> users;
    Integer role;
    Integer department;
}
