package com.zjuse.bankingsystem.service.loan;

import com.zjuse.bankingsystem.entity.loan.Officer;
import com.zjuse.bankingsystem.mapper.loan.OfficerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomOfficerDetailsService implements UserDetailsService {

    @Autowired
    private OfficerMapper officerMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Officer officer = officerMapper.findByUsername(username);
        if (officer == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.withUsername(officer.getUsername())
                .password(officer.getPassword())
                .authorities("USER") // You can customize roles here
                .build();
    }
}
