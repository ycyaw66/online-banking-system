package com.zjuse.bankingsystem.service.loan;

import com.zjuse.bankingsystem.entity.loan.Manager;
import com.zjuse.bankingsystem.mapper.loan.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomManagerDetailsService implements UserDetailsService {

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerMapper.findByUsername(username);
        if (manager == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.withUsername(manager.getUsername())
                .password(manager.getPassword())
                .authorities("USER") // You can customize roles here
                .build();
    }
}