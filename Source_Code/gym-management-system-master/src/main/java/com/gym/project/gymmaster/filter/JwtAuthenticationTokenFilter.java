package com.gym.project.gymmaster.filter;//package com.gymmaster.filter;
//
//import com.gymmaster.entity.LoginUser;
//import com.gymmaster.utils.JwtUtil;
//import com.gymmaster.utils.RedisCache;
//import io.jsonwebtoken.Claims;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.rmi.RemoteException;
//import java.util.Objects;
//
//@Component
//public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
//
//    @Autowired
//    RedisCache redisCache;
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        // get token
//         String token = httpServletRequest.getHeader("token");
//        if(token == null || token.length() == 0){
//            filterChain.doFilter(httpServletRequest,httpServletResponse);
//            return;
//        }
//        // analyse token
//        String userid;
//        try {
//            Claims claims = JwtUtil.parseJWT(token);
//            userid = claims.getSubject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw  new RuntimeException("illegal token");
//        }
//        String redisKey = "login"+userid;
//
//        // get information from redis
//        LoginUser user = redisCache.getCacheObject(redisKey);
//        if (Objects.isNull(user)){
//            throw new RemoteException("not logged in");
//        }
//        //存入SecurityContextHolder
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        filterChain.doFilter(httpServletRequest,httpServletResponse);
//    }
//}
