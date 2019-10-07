package com.example.community.service;

import com.example.community.mapper.UserMapper;
import com.example.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        User dbUser=userMapper.findByAccountId(user.getAccountId());
        if (dbUser!=null){
            //更新
            user.setGmtModified(System.currentTimeMillis());
            user.setName(user.getName());
            user.setToken(user.getToken());
            user.setAvatarUrl(user.getAvatarUrl());
            userMapper.update(user);
            return;
        }else {
            userMapper.insert(user);
            //创建
            return;
        }
    }
}
