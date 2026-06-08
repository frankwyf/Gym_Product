package com.gym.project.gymmaster.config;//package com.gymmaster.config;
//
//import com.gymmaster.filter.JwtAuthenticationTokenFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    //创建BCryptPasswordEncoder注入容器
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
////testing password encoder
////    public static void main(String[] args) {
////        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//////        String encode = passwordEncoder.encode("aaa");
//////        System.out.println(encode);
////        boolean matches = passwordEncoder.matches(
////                "aaa",
////                "$2a$10$qYDIdwb/X2qVdoK6.L0uNe.jpJB/80O0tW4SpneTQ6IOfdc22rojC");
////        // $2a$10$qYDIdwb/X2qVdoK6.L0uNe.jpJB/80O0tW4SpneTQ6IOfdc22rojC
////        System.out.println(matches);
////    }
//    @Autowired
//    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                //关闭csrf
//                .csrf().disable()
//                //不通过Session获取SecurityContext
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // 对于登录接口 允许匿名访问
//                .antMatchers("/customer/login").anonymous()
//                .antMatchers("/venue/getAvailableVenues").anonymous()
//                .antMatchers("/venue/getById").anonymous()
////                .antMatchers("/testCors").hasAuthority("system:dept:list222")
//                // 除上面外的所有请求全部需要鉴权认证
//                .anyRequest().authenticated();
//
//        //添加过滤器
//        http.addFilterBefore(jwtAuthenticationTokenFilter,UsernamePasswordAuthenticationFilter.class);
//    }
////
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//}
