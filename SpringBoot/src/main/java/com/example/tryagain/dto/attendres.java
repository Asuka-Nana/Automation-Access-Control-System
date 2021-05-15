package com.example.tryagain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class attendres {
    List<List<String>> date;
    Integer monres;
    Integer yeares;
    Integer days;
}
