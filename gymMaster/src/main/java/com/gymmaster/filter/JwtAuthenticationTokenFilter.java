package com.gymmaster.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.gymmaster.entity.LoginUser;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * Servlet filter that validates the JWT on every request.
 * Supports both the legacy {@code token} header and the standard
 * {@code Authorization: Bearer <token>} header.
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token == null || token.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token.");
        } catch (Exception e) {
            log.warn("Failed to parse JWT token", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
        }

        String redisKey = "login" + userid;
        LoginUser user = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(user)) {
            // Token valid but session expired from Redis — return 401, not 500.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session expired, please login again.");
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    /** Supports {@code token} header (legacy) and {@code Authorization: Bearer} header. */
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null && !token.isEmpty()) {
            return token;
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
