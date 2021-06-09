package com.example.tryagain.controller;

import com.example.tryagain.dto.*;
import com.example.tryagain.mapper.CommentMapper;
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
public class CommentController {

    @Autowired
    NoticeMapper noticeMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/getcomment")
    @RequiresPermissions("0")
    public CommentRes getComment (@RequestHeader("Authorization") String token , @RequestBody getcommentp id){
        String user = parsingtoken.Parsing(token);
        Integer nid = id.getNid();
        User userDeatail = userMapper.findpwdbyname(user);
        Integer department = userDeatail.getDepartment();
        Integer role = userDeatail.getState();
        Integer ndep = noticeMapper.getdepbyid(nid);
        if(ndep == null){
            return new CommentRes(null,null,-3);
        }
        if(role != 2 && (ndep != department ) && (ndep != 10)){
            return new CommentRes(null,null,-1);
        }
        notice noticeex = noticeMapper.getnoticebyid(nid);
        InputStream inputStream = null;
        byte[] buffer = null;
        String path = "D:\\data\\"+noticeex.getUsername()+".jpg";
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
        NoticeRes noticeRes = new NoticeRes(noticeex.getId(),noticeex.getUsername(),noticeex.getTime(),noticeex.getContent(),src);
        CommentDetail[] commentDetails = commentMapper.getcomment(nid);
        if(commentDetails.length == 0){
            return new CommentRes(noticeRes,null,-2);
        }
        List<Comment> comments = new LinkedList<>() ;
        for (Integer i = 0 ; i< commentDetails.length; i++){
            InputStream inputStream1 = null;
            byte[] buffer1 = null;
            String path1 = "D:\\data\\"+commentDetails[i].getUsername()+".jpg";
            File file1 = new File(path);
            if(!file1.exists()){
                path1 = "D:\\data\\def.jpg";
            }
            //读取图片字节数组
            try {
                inputStream1 = new FileInputStream(path1);
                int count1 = 0;
                while (count1 == 0) {
                    count1 = inputStream1.available();
                }
                buffer1 = new byte[count1];
                inputStream1.read(buffer1);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream1 != null) {
                    try {
                        // 关闭inputStream流
                        inputStream1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String src1 = Base64.getEncoder().encodeToString(buffer1);
            comments.add(new Comment(commentDetails[i].getCid(),commentDetails[i].getUsername(),commentDetails[i].getTime(),commentDetails[i].getContent(),src1));
        }
        return new CommentRes(noticeRes,comments,1);
    }

    @RequestMapping("/addcomment")
    @RequiresPermissions("0")
    public Integer addComment(@RequestHeader("Authorization") String token, @RequestBody addCommentpara comments){
        String user = parsingtoken.Parsing(token);
        commentMapper.addcomment(user,comments.getTime(),comments.getComment(),comments.getId());
        return 1;
    }

}
