package com.example.tryagain.config;

import com.example.tryagain.mapper.UserMapper;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.louislivi.fastdep.shirojwt.shiro.FastDepShiroJwtAuthorization;

import java.util.HashSet;
import java.util.Set;

@Component
public class FastDepShiroJwtConfig  extends FastDepShiroJwtAuthorization {

    @Autowired
    private UserMapper userMapper;

    @Override
    public SimpleAuthorizationInfo getAuthorizationInfo(String userId) {
        String collect = userMapper.findpwdbyname(userId).getState().toString();
        Set<String> permissions = new HashSet<>();
        permissions.add(collect);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        System.out.println(collect);
        simpleAuthorizationInfo.addStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }
}

