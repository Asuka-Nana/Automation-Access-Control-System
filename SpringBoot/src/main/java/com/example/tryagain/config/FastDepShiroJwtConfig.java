package com.example.tryagain.config;

import com.example.tryagain.mapper.RolesMapper;
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

    @Autowired
    private RolesMapper rolesMapper;

    @Override
    public SimpleAuthorizationInfo getAuthorizationInfo(String userId) {
        Integer collect = userMapper.findpwdbyname(userId).getState();
        Set<String> permissions = new HashSet<>();
        Integer[] priv = rolesMapper.getPrivilege(collect);
        for(Integer i = 0; i < priv.length; i++){
            permissions.add(priv[i].toString());
            System.out.println(priv[i].toString());
        }
        //permissions.add(collect);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        simpleAuthorizationInfo.addStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }
}

