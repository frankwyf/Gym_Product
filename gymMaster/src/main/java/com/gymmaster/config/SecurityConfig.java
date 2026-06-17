package com.gymmaster.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gymmaster.filter.JwtAuthenticationTokenFilter;
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //创建BCryptPasswordEncoder注入容器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//testing password encoder
//    public static void main(String[] args) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////        String encode = passwordEncoder.encode("aaa");
////        System.out.println(encode);
//        boolean matches = passwordEncoder.matches(
//                "aaa",
//                "$2a$10$qYDIdwb/X2qVdoK6.L0uNe.jpJB/80O0tW4SpneTQ6IOfdc22rojC");
//        // $2a$10$qYDIdwb/X2qVdoK6.L0uNe.jpJB/80O0tW4SpneTQ6IOfdc22rojC
//        System.out.println(matches);
//    }
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // turn off csrf
                .csrf().disable()
                // not using session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // use loginCus to login
                .antMatchers("/loginCus/**").permitAll()
                .antMatchers("/health/**").permitAll()
                .antMatchers("/customer/register").anonymous()
                .antMatchers("/getCaptcha").anonymous()
                .antMatchers("/venue/**").permitAll()
                .antMatchers("/reservation/**").permitAll()
                .antMatchers("/getCaptchaReset").permitAll()
                .antMatchers("/static/**",
                                        "/**/*.jpg",
                                        "/**/*.JPG",
                                        "/**/*.mp4",
                                        "/**/*.png",
                                        "/templates.error/*",
                                        "/until/**",
                                        "/venue/getById",
                                        "/venue/getAvailableVenues").permitAll()
                // ask for authentication for everything else
                .anyRequest().authenticated();


        //添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter,UsernamePasswordAuthenticationFilter.class);
        //配置异常处理器
        http.exceptionHandling()
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler);
        //允许跨域
        http.cors();
    }
//
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
