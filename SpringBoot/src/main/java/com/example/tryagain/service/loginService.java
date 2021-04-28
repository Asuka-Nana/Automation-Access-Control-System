package com.example.tryagain.service;

import com.example.tryagain.dto.logRes;
import com.example.tryagain.pojo.User;

public interface loginService {
    logRes verifypwdbyname (String username, String password);
}
