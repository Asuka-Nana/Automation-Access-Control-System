package com.example.tryagain.controller;

import com.example.tryagain.dto.NoticeRes;
import com.example.tryagain.dto.addNoticepara;
import com.example.tryagain.mapper.NoticeMapper;
import com.example.tryagain.mapper.UserMapper;
import com.example.tryagain.pojo.User;
import com.example.tryagain.pojo.notice;
import com.example.tryagain.util.parsingtoken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@RestController
public class NoticeController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    NoticeMapper noticeMapper;

    @RequestMapping("/getnotice")
    @RequiresPermissions("0")
    public List<NoticeRes> getnotice(@RequestHeader("Authorization") String token){
        String user = parsingtoken.Parsing(token);
        Integer department = userMapper.findpwdbyname(user).getDepartment();
        notice[] notices = noticeMapper.getnotice(department);
        List<NoticeRes> res = new LinkedList<>();
        for(Integer i = 0; i < notices.length ; i++){
            InputStream inputStream = null;
            byte[] buffer = null;
            String path = "D:\\data\\"+notices[i].getUsername()+".jpg";
            File file = new File(path);
            if(!file.exists()){
                path = "D:\\data\\def.jpg";
            }
            //读取图片字节数组
            try {
                inputStream = new FileInputStream(path);
                int count = 0;
                while (count == 0) {
                    count = inputStream.available();
                }
                buffer = new byte[count];
                inputStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        // 关闭inputStream流
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String src = Base64.getEncoder().encodeToString(buffer);
            res.add(new NoticeRes(notices[i].getId(),notices[i].getUsername(),notices[i].getTime(),notices[i].getContent(),src));
        }
        return res;
    }

    @RequestMapping("/addNotice")
    @RequiresPermissions("1")
    public Integer addNotice(@RequestHeader("Authorization") String token, @RequestBody addNoticepara noticepara){
        String user = parsingtoken.Parsing(token);
        User userdetail = userMapper.findpwdbyname(user);
        Integer department = userdetail.getDepartment();
        Integer role = userdetail.getState();
        if(role == 0){
            return 1;
        }
        //System.out.println(noticepara.getTime());
        //System.out.println(noticepara.getNotice());
        noticeMapper.addnotice(user,noticepara.getTime(),department,noticepara.getNotice());
        return 0;
    }

    @RequestMapping("/getrole")
    public Integer getRole(@RequestHeader("Authorization") String token){
        String user = parsingtoken.Parsing(token);
        return userMapper.findpwdbyname(user).getState();
    }
}
