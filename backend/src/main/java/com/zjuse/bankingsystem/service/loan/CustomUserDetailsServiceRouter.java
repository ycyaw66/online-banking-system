package com.zjuse.bankingsystem.service.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceRouter {

    @Autowired
    @Qualifier("customManagerDetailsService")
    private UserDetailsService managerDetailsService;

    @Autowired
    @Qualifier("customOfficerDetailsService")
    private UserDetailsService officerDetailsService;

    public UserDetails loadManagerByUsername(String username) throws UsernameNotFoundException {
        return managerDetailsService.loadUserByUsername(username);
    }

    public UserDetails loadOfficerByUsername(String username) throws UsernameNotFoundException {
        return officerDetailsService.loadUserByUsername(username);
    }
}
