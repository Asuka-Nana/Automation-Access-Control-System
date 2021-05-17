package com.example.tryagain.controller;

import com.example.tryagain.dto.UsersRes;
import com.example.tryagain.dto.Visituser;
import com.example.tryagain.mapper.UserMapper;
import com.example.tryagain.pojo.User;
import com.example.tryagain.util.parsingtoken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class UsersController {
    @Autowired
    UserMapper userMapper;

    @RequestMapping("/getuser")
    @RequiresPermissions("0")
    public UsersRes getuser (@RequestHeader("Authorization") String token){
        String user = parsingtoken.Parsing(token);
        Integer department = userMapper.findpwdbyname(user).getDepartment();
        User[] users = userMapper.getusers(department);
        List<List<String>> res = new LinkedList<>();
        for (Integer i = 0; i < users.length;i++){
            List<String> tmp = new LinkedList<>();
            tmp.add(users[i].getUsername());
            tmp.add(users[i].getName());
            tmp.add(users[i].getState().toString());
            res.add(tmp);
        }
        return new UsersRes(res,userMapper.findpwdbyname(user).getState(),department);
    }


    @RequestMapping("/deleteuser")
    @RequiresPermissions("1")
    public Integer deleteuser (@RequestHeader("Authorization") String token, @RequestBody Visituser visituser){
        String user = parsingtoken.Parsing(token);
        Integer mydep = userMapper.findpwdbyname(user).getDepartment();
        Integer myrole = userMapper.findpwdbyname(user).getState();
        Integer othdep = userMapper.findpwdbyname(visituser.getUsername()).getDepartment();
        Integer othrole = userMapper.findpwdbyname(visituser.getUsername()).getState();
        if((myrole == 2) || (mydep == othdep && myrole == 1 && othrole == 0)){
            userMapper.deleteuser(visituser.getUsername());
            return 1;
        }
        return 0;
    }
}
