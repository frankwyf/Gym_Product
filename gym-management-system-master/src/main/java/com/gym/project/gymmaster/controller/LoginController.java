package com.gym.project.gymmaster.controller;//package com.gymmaster.controller;
//
//import com.gymmaster.common.BackMsg;
//import com.gymmaster.entity.Customer;
//import com.gymmaster.entity.LoginUser;
//import com.gymmaster.service.CustomerService;
//import com.gymmaster.utils.JwtUtil;
//import com.gymmaster.utils.RedisCache;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@RestController
//@RequestMapping("/customer")
//public class LoginController {
//    @Autowired
//    CustomerService customerService;
//    @Autowired
//    AuthenticationManager authenticationManager;
//    @Autowired
//    RedisCache redisCache;
//    @PostMapping("/login")
//    public BackMsg login(@RequestBody Customer customer){
//        // 获取authenticationManager
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword());
//
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        //认证未通过
//        if(Objects.isNull(authenticate)){
//            throw new RuntimeException("login failed");
//        }
//        //认证通过。生成jwt
//        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
//        int id = loginUser.getCustomer().getUid();
//        String uid = Integer.toString(id);
//        String jwt = JwtUtil.createJWT(uid);
//
//        //存入redis
//        Map<String,String> map = new HashMap<>();
//        map.put("token"+uid,jwt);
//        redisCache.setCacheObject("login"+uid,loginUser);
//
//        return BackMsg.success(map);
//    }
//
//    @PostMapping("/logout")
//    public BackMsg logout(){
//        //获取对应userid
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
//        LoginUser customer = (LoginUser) authenticationToken.getPrincipal();
//        int id = customer.getCustomer().getUid();
//
//        //删除redis中的值
//        redisCache.deleteObject("login"+id);
//        return BackMsg.success("logout successfully");
//    }
//}
