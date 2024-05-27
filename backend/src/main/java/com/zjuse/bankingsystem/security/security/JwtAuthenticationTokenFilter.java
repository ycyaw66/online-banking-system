package com.zjuse.bankingsystem.security.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.service.dto.OnlineUserDto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider; 
    @Autowired
    private JwtConfig jwtConfig; 

    @Autowired
    private UserDetailsService userDetailsService; 

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("---------Start Jwt authentication token filter---------");
        String authHeader = request.getHeader(jwtConfig.getHeader()); 
        if (Objects.isNull(authHeader) || !authHeader.startsWith(jwtConfig.getTokenStartWith())) {
            filterChain.doFilter(request, response);
            log.info("非法 token: {}", authHeader);
            return ;
        }

        String authToken = authHeader.split(" ")[1];
        log.info("authToken: {}", authToken);
        if (StrUtil.isBlank(authToken)|| !JWTUtil.verify(authToken, jwtConfig.getTokenSignKey().getBytes(StandardCharsets.UTF_8))) {
            filterChain.doFilter(request, response);
            return ;
        }


        Authentication authentication = jwtTokenProvider.getAuthentication(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
        log.info("---------End Jwt authentication token filter---------");
    }
}
