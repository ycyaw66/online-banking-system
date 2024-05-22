package com.zjuse.bankingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.mapper.UserMapper;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper; 

    public boolean registerNewUser(User user) {
        System.out.println(user);
        userMapper.insert(user);
        System.out.println(user);
        return true;
    }
}
