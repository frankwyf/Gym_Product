package com.gym.project.gymmaster.filter;//package com.gymmaster.filter;
//
//
//
//import com.alibaba.fastjson.JSON;
//import com.gymmaster.common.BackMsg;
//import com.gymmaster.common.ThreadContext;
//import com.gymmaster.entity.Logs;
//import com.gymmaster.service.LogService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.AntPathMatcher;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//
//
//
///**
// * check for login and filter the request
// * @author Group 2 of XJCO2913
// * @version 1.0
// * @since 2023/2/23
// */
//@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
//@Slf4j
//public class LoginFilter implements Filter{
//    //路径匹配器，支持通配符
//    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
//    @Autowired
//    LogService logService;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        //1、获取本次请求的URI
//        String requestURI = request.getRequestURI();// /backend/index.html
//
//        log.info("拦截到请求：{}",requestURI);
//
//        //定义不需要处理的请求路径
//        String[] urls = new String[]{
//                "/employee/login",
//                "/employee/logout",
//                "/employee/register",
//                "/manager/login",
//                "/manager/logout",
//                "/manager/register",
//                "/coach/login",
//                "/coach/logout",
//                "/coach/register",
//                "/static/**",
//                "/until/**",
//        };
//
//        //2、判断本次请求是否需要处理
//        boolean check = check(urls, requestURI);
//
//        //3、如果不需要处理，则直接放行
//        if(check){
//            log.info("本次请求{}不需要处理",requestURI);
//            filterChain.doFilter(request,response);
//            return;
//        }
//        Logs logs = new Logs();
//        //4-1、判断登录状态，如果已登录，则直接放行
//        if(request.getSession().getAttribute("employee") != null){
//            log.info("id: {}",request.getSession().getAttribute("employee"));
//
//            int empId = (int) request.getSession().getAttribute("employee");
//
//            logs.setType("employee");
//            logs.setUid(empId);
//            logs.setDate(LocalDateTime.now());
//            logService.save(logs);
//            ThreadContext.setCurrentId(empId);
//            ThreadContext.setCurrentType("employee");
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        //4-2、判断登录状态，如果已登录，则直接放行
//        if(request.getSession().getAttribute("manager") != null){
//            log.info("id: {}",request.getSession().getAttribute("manager"));
//
//            int managerId = (int) request.getSession().getAttribute("manager");
//            ThreadContext.setCurrentId(managerId);
//            ThreadContext.setCurrentType("manager");
//            logs.setType("manager");
//            logs.setUid(managerId);
//            logs.setDate(LocalDateTime.now());
//            logService.save(logs);
//            filterChain.doFilter(request,response);
//            return;
//        }
//        if(request.getSession().getAttribute("coach") != null){
//            log.info("id: {}",request.getSession().getAttribute("coach"));
//
//            int coachId = (int) request.getSession().getAttribute("coach");
//            ThreadContext.setCurrentId(coachId);
//            ThreadContext.setCurrentType("coach");
//            logs.setType("coach");
//            logs.setUid(coachId);
//            logs.setDate(LocalDateTime.now());
//            logService.save(logs);
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        log.info("not logged in");
//        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
//        response.getWriter().write(JSON.toJSONString(BackMsg.error("NOTLOGIN")));
//        return;
//
//    }
//
//
//    public boolean check(String[] urls,String requestURI){
//        for (String url : urls) {
//            boolean match = PATH_MATCHER.match(url, requestURI);
//            if(match){
//                return true;
//            }
//        }
//        return false;
//    }
//}
