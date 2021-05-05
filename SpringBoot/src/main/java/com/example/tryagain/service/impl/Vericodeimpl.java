package com.example.tryagain.service.impl;

import com.example.tryagain.dto.Vericode;
import com.example.tryagain.service.codeService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.louislivi.fastdep.shirojwt.jwt.JwtUtil;
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
    private JwtUtil jwtUtil;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Override
    public Vericode getvericode (){
        String time = String.valueOf(System.currentTimeMillis());
        String token = jwtUtil.sign(time);
        String createText = defaultKaptcha.createText();
        BufferedImage challenge = defaultKaptcha.createImage(createText);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try{
        ImageIO.write(challenge, "jpg", stream);
        }
        catch (IOException e){

        }
        String s = Base64.getEncoder().encodeToString(stream.toByteArray());
        return Vericode.example(token,s);
    }
}
