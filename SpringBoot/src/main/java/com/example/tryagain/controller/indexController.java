package com.example.tryagain.controller;

import com.example.tryagain.util.parsingtoken;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
public class indexController {
    @RequestMapping("/img")
    public void getUserImg(@RequestParam("file") MultipartFile file , @RequestHeader("Authorization") String token){
        if (file==null || file.isEmpty()) return ;
        //System.out.println("name: " + fileHandler.getOriginalFilename() + "  size: " + fileHandler.getSize());

        String pathName = "D:\\data\\";
        String[] tmp = file.getContentType().split("/");
        String type = "." + tmp[1];
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
}

