package com.gymmaster.filter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gymmaster.entity.LoginUser;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;

import io.jsonwebtoken.Claims;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // Keep backward compatibility with legacy `token` header and support `Authorization: Bearer ...`.
        String token = resolveToken(httpServletRequest);
        if(token == null || token.length() == 0){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        // analyse token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            log.warn("Failed to parse JWT token", e);
            throw  new RuntimeException("illegal token");
        }
        String redisKey = "login"+userid;

        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(user)){
            throw new RemoteException("not logged in");
        }
        // save SecurityContextHolder
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null && token.length() > 0) {
            return token;
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
