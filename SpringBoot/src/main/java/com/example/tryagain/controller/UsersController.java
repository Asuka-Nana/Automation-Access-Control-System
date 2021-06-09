package com.example.tryagain.controller;

import com.example.tryagain.dto.AddUser;
import com.example.tryagain.dto.UsersRes;
import com.example.tryagain.dto.Visituser;
import com.example.tryagain.mapper.UserMapper;
import com.example.tryagain.pojo.User;
import com.example.tryagain.util.RWFile;
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
        User[] users ;
        if(userMapper.findpwdbyname(user).getState() == 2){
            users = userMapper.getallusers();
        }
        else{
            users = userMapper.getusers(department);
        }
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
        if((myrole == 2 && (othrole == 0 || othrole == 1)) || (mydep == othdep && myrole == 1 && othrole == 0)){
            userMapper.deleteuser(visituser.getUsername());
            return 1;
        }
        return 0;
    }

    @RequestMapping("/adduser")
    @RequiresPermissions("1")
    public Integer adduser (@RequestHeader("Authorization") String token, @RequestBody AddUser addUser){
        String username = parsingtoken.Parsing(token);
        User user = userMapper.findpwdbyname(username);
        if(addUser.getRole() == 2){
            return 0;
        }
        if (user.getState() == 2 ||(user.getState() == 1 && user.getDepartment() == addUser.getDepartment() && addUser.getRole() == 0)){
            if (userMapper.findpwdbyname(addUser.getUsername()) != null){
                return 2;
            }
            userMapper.adduser(addUser.getUsername(),addUser.getName(),addUser.getRole(),addUser.getDepartment(),addUser.getEmail(),addUser.getTelephone(),addUser.getLocation(),addUser.getDiscription());
            //RWFile.RWimg(addUser.getUsername());
            return 1;
        }
        return 0;
    }
}
