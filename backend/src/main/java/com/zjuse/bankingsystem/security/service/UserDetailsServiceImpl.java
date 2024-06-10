package com.zjuse.bankingsystem.security.service;

import java.util.Collections;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.entity.Admin;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardInspector;
import com.zjuse.bankingsystem.entity.user.User;
import com.zjuse.bankingsystem.security.security.enums.LoginType;
import com.zjuse.bankingsystem.security.service.dto.AuthorityDto;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.service.creditCard.InspectorService;
import com.zjuse.bankingsystem.service.user.UserService;
import com.zjuse.bankingsystem.utils.ApiResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserCacheManager userCacheManager; 
    @Autowired
    private UserService userService; 
    @Autowired 
    private AdminService adminService; 
    @Autowired
    private InspectorService inspectorService; 

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if (Objects.isNull(jwtUserDto)) {
            try {
                LoginType loginType = LoginType.valueOf(username.split("-")[0]); 
                switch (loginType) {
                    case USER:
                        User user = (User) userService.getUserByUsername(username.split("-")[1]).payload;
                        if (user == null) {
                            return null;
                        } else {
                            jwtUserDto = new JwtUserDto(
                                user.getUsername(),
                                user.getPassword(),
                                null
                            );
                        }
                        break;
                    case ADMIN:
                        Admin admin = (Admin) adminService.getUserByUsername(username.split("-")[1]).payload; 
                        if (admin == null) {
                            return null;
                        } else {
                            jwtUserDto = new JwtUserDto(
                                admin.getUsername(),
                                admin.getPassword(),
                                Collections.singletonList(new AuthorityDto("ADMIN"))
                            );
                        }
                        break ; 
                    case INSPECTOR:
                        CreditCardInspector creditCardInspector = (CreditCardInspector) inspectorService.getInspectorByUsername(username.split("-")[1]).payload;
                        if (creditCardInspector == null) {
                            return null; 
                        } else {
                            jwtUserDto = new JwtUserDto(
                                creditCardInspector.getName(), 
                                creditCardInspector.getPassword(), 
                                Collections.singletonList(new AuthorityDto("INSPECTOR"))
                            );
                        }
                        break;
                    default:
                        return null; 
                }
                
            } catch(Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
            userCacheManager.addUserCache(username, jwtUserDto);
        }
        return jwtUserDto;
    }
}
