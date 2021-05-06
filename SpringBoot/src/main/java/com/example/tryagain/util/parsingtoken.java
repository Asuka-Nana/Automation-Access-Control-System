package com.example.tryagain.util;

import com.example.tryagain.pojo.Usertoken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;

public class parsingtoken {
    public static String Parsing (String token){
        System.out.println(token);
        String s = token;
        s = s.replace("Bearer","");
        String[] tmp = s.split("\\.");
        System.out.println(tmp.length);
        Base64 decoder = new Base64(true);
        byte[] decodedBytes = decoder.decode(tmp[1]);
        String res = new String(decodedBytes);
        System.out.println(res);
        String ans = null;
        try{
            Usertoken usertoken = new ObjectMapper().readValue(ans, Usertoken.class);
            ans = usertoken.getSub();
        }
        catch( com.fasterxml.jackson.core.JsonProcessingException e){
        }
        System.out.println(ans);
        return ans;
    }
}
