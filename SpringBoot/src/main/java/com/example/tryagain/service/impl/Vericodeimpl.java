package com.example.tryagain.service.impl;

import com.example.tryagain.dto.Vericode;
import com.example.tryagain.service.codeService;
import com.example.tryagain.util.sha;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service("codeService")
public class Vericodeimpl implements codeService {

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Override
    public Vericode getvericode (){
        String time = String.valueOf(System.currentTimeMillis());
        String createText = defaultKaptcha.createText();
        BufferedImage challenge = defaultKaptcha.createImage(createText);
        String sh_code = sha.SHA1(createText);
        System.out.println(sh_code);
        //String token = jwtUtil.sign(sh_code);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try{
        ImageIO.write(challenge, "jpg", stream);
        }
        catch (IOException e){

        }
        String s = Base64.getEncoder().encodeToString(stream.toByteArray());
        return Vericode.example(sh_code,s);
    }
}
