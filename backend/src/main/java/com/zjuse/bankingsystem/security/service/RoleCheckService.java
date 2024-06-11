package com.zjuse.bankingsystem.security.service;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.zjuse.bankingsystem.security.service.dto.AuthorityDto;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("roleCheck")
public class RoleCheckService {
    public boolean isRole(String role) {
        if (StrUtil.isBlank(role)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
        log.info((String)authentication.getCredentials());
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info(authorities.toString());
		return authorities.contains(new AuthorityDto(role)); 
    }
}
