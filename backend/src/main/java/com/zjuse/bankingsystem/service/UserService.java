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
        try {
            userMapper.insert(user);
            return true; 
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false; 
        }
    }
}
