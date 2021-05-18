package com.example.tryagain.controller;

import com.example.tryagain.dto.ChangeDetail;
import com.example.tryagain.dto.Date;
import com.example.tryagain.dto.attendres;
import com.example.tryagain.dto.demntattRea;
import com.example.tryagain.mapper.AttendanceMapper;
import com.example.tryagain.mapper.UserMapper;
import com.example.tryagain.pojo.User;
import com.example.tryagain.util.parsingtoken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AttendanceController {

    @Autowired
    AttendanceMapper attendanceMapper;

    @Autowired
    UserMapper userMapper;
    Map<String, Integer> days = new HashMap<String,Integer>();

    public void ini(){
        days.put("01",31);
        days.put("02",28);
        days.put("03",31);
        days.put("04",30);
        days.put("05",31);
        days.put("06",30);
        days.put("07",31);
        days.put("08",31);
        days.put("09",30);
        days.put("10",31);
        days.put("11",30);
        days.put("12",31);
    }

    @RequestMapping("/getatt")
    @RequiresPermissions("0")
    public attendres getattend(@RequestHeader("Authorization") String token , @RequestBody Date date){
        ini();
        String user = parsingtoken.Parsing(token);
        String dateini = date.getDate();
        System.out.println(dateini.split("-")[1]);
        System.out.println(dateini);
        String dateb1 = dateini + "-01";
        String datee1 = dateini + '-' + days.get(dateini.split("-")[1]).toString();
        String[] res1 = attendanceMapper.getattend(user,dateb1,datee1);
        String dateb2 = dateini.split("-")[0] + "-01-01";
        String datae2 = dateini.split("-")[0] + "-12-31";
        String[] res2 = attendanceMapper.getattend(user,dateb2,datae2);
        Integer res3 = res2.length;
        List<List<String>> res4 = new LinkedList<>();
        Integer res5 = res1.length;
        for(Integer i = 1; i <= days.get(dateini.split("-")[1]);i ++){
            List<String> tmp = new LinkedList<>();
            String temp;
            if (i < 10){
                temp = dateini + "-0" + i.toString();
            }
            else {
                temp = dateini + "-" + i.toString();
            }
            tmp.add(temp);
            if (Arrays.asList(res1).contains(temp)){
                tmp.add("2");
            }
            else {
                tmp.add("");
            }
            res4.add(tmp);
        }
        return new attendres(res4,res5,res3,days.get(dateini.split("-")[1]));
    }

    @RequestMapping("/getallatt")
    @RequiresPermissions("1")
    public List<demntattRea> getallatt(@RequestHeader("Authorization") String token, @RequestBody Date date){
        ini();
        String user = parsingtoken.Parsing(token);
        List<demntattRea> res = new LinkedList<>();
        Integer role = userMapper.findpwdbyname(user).getState();
        if (role == 2){
            User[] users = userMapper.getallusers();
            for (Integer i = 0; i< users.length;i++){
                String dateini = date.getDate();
                String dateb1 = dateini + "-01";
                String datee1 = dateini + '-' + days.get(dateini.split("-")[1]).toString();
                Integer res1 = attendanceMapper.getattend(users[i].getUsername(),dateb1,datee1).length;
                String dateb2 = dateini.split("-")[0] + "-01-01";
                String datae2 = dateini.split("-")[0] + "-12-31";
                Integer res2 = attendanceMapper.getattend(users[i].getUsername(),dateb2,datae2).length;
                res.add(new demntattRea(users[i].getUsername(),res1,res2));
            }
            return res;
        }
        if (role == 1){
            Integer department = userMapper.findpwdbyname(user).getDepartment();
            User[] users = userMapper.getusers(department);
            for (Integer i = 0; i< users.length;i++){
                String dateini = date.getDate();
                String dateb1 = dateini + "-01";
                String datee1 = dateini + '-' + days.get(dateini.split("-")[1]).toString();
                Integer res1 = attendanceMapper.getattend(users[i].getUsername(),dateb1,datee1).length;
                String dateb2 = dateini.split("-")[0] + "-01-01";
                String datae2 = dateini.split("-")[0] + "-12-31";
                Integer res2 = attendanceMapper.getattend(users[i].getUsername(),dateb2,datae2).length;
                res.add(new demntattRea(users[i].getUsername(),res1,res2));
            }
            return res;
        }
        return res;
    }

}
