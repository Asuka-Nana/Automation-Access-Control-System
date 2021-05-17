package com.example.tryagain.controller;

import com.example.tryagain.dto.ChangeDetail;
import com.example.tryagain.dto.Password;
import com.example.tryagain.dto.Visituser;
import com.example.tryagain.pojo.UserDetail;
import com.example.tryagain.mapper.UserMapper;
import com.example.tryagain.service.UserDetailService;
import com.example.tryagain.util.parsingtoken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class indexController {
    @RequestMapping("/img")
    @RequiresPermissions("0")
    public void getUserImg(@RequestParam("file") MultipartFile file , @RequestHeader("Authorization") String token){
        if (file==null || file.isEmpty()) return ;
        //System.out.println("name: " + fileHandler.getOriginalFilename() + "  size: " + fileHandler.getSize());

        String pathName = "D:\\data\\";
        String[] tmp = file.getContentType().split("/");
        String type = "." + "jpg";
        String name = parsingtoken.Parsing(token);
        String picFullFileName = pathName + name + type;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(picFullFileName);
            fos.write(file.getBytes()); // 写入文件
            return ;
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Autowired
    UserDetailService userDetailService;

    @RequestMapping("/dis")
    @RequiresPermissions("0")
    public UserDetail retdeatil (@RequestHeader("Authorization") String token ){
        String user = parsingtoken.Parsing(token);
        return userDetailService.getdetail(user);
    }

    @RequestMapping("/visit")
    @RequiresPermissions("0")
    public UserDetail visituser (@RequestBody Visituser visituser){
        return userDetailService.getdetail(visituser.getUsername());
    }




    @Autowired
    UserMapper userMapper;

    @RequestMapping("/changepwd")
    @RequiresPermissions("0")
    public Integer changepwd(@RequestHeader("Authorization") String token , @RequestBody Password pwd){
        String user = parsingtoken.Parsing(token);
        String pwdcorr = userMapper.findpwdbyname(user).getPassword();
        if (pwd.getPassword().equals(pwdcorr)){
            userMapper.changepwd(user,pwd.getNewpassword());
            return 1;
        }
        else{
            return 0;
        }
    }

    @RequestMapping("/changedetail")
    @RequiresPermissions("0")
    public Integer changedetail(@RequestHeader("Authorization") String token , @RequestBody ChangeDetail changeDetail){
        String user = parsingtoken.Parsing(token);
        String name = changeDetail.getName();
        String email = changeDetail.getEmail();
        String telephone = changeDetail.getTelephone();
        String location = changeDetail.getLocation();
        String discription = changeDetail.getDiscription();
        userMapper.changedetail(user,name,email,telephone,location,discription);
        return 1;
    }
}

