package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.LoginUser;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.CustomerService;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/loginCus")
@SuppressWarnings("null")
public class LoginController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final RedisCache redisCache;

    @PostMapping("/login")
    public BackMsg<Map<String, String>> login(@Valid @RequestBody Customer customer) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(token);
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            String uid = String.valueOf(loginUser.getCustomer().getUid());
            String jwt = JwtUtil.createJWT(uid);
            redisCache.setCacheObject("login" + uid, loginUser, 30, TimeUnit.MINUTES);

            Map<String, String> map = new HashMap<>();
            map.put("token", jwt);
            return BackMsg.success(map);
        } catch (AuthenticationException e) {
            return BackMsg.error("Invalid username or password.");
        } catch (RuntimeException e) {
            log.warn("Login failed for user '{}': {}", customer.getUsername(), e.getMessage());
            return BackMsg.error("Login failed. Please try again.");
        }
    }

    @GetMapping("/logout")
    public BackMsg<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            redisCache.deleteObject("login" + loginUser.getCustomer().getUid());
        }
        return BackMsg.success("Logged out successfully.");
    }

    /**
     * Step 1 of password reset: verify the account exists and send a reset token via email.
     * The token is stored in Redis with a 15-minute TTL.
     *
     * NOTE: This endpoint intentionally returns the same response whether the email exists or not
     * to prevent user enumeration (OWASP A07).
     */
    @PostMapping("/requestPasswordReset")
    public BackMsg<String> requestPasswordReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            throw new BusinessException("Email is required.");
        }
        LambdaQueryWrapper<Customer> qw = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getEmail, email);
        Customer customer = customerService.getOne(qw);
        if (customer != null) {
            String resetToken = UUID.randomUUID().toString().replace("-", "");
            // 15-minute expiry reset token keyed by username
            redisCache.setCacheObject("pwdreset:" + customer.getUsername(), resetToken, 15, TimeUnit.MINUTES);
            log.info("Password reset token issued for user '{}'", customer.getUsername());
            // In a real system, send the token via email here.
        }
        // Always return success to prevent user enumeration.
        return BackMsg.success("If an account with that email exists, a reset link has been sent.");
    }

    /**
     * Step 2 of password reset: submit the token and new password.
     */
    @PostMapping("/resetPassword")
    public BackMsg<String> resetPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String token    = body.get("token");
        String newPass  = body.get("newPassword");
        if (username == null || token == null || newPass == null) {
            throw new BusinessException("username, token and newPassword are all required.");
        }
        String redisKey   = "pwdreset:" + username;
        String storedToken = redisCache.getCacheObject(redisKey);
        if (storedToken == null || !storedToken.equals(token)) {
            throw new BusinessException("Invalid or expired reset token.");
        }
        LambdaQueryWrapper<Customer> qw = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUsername, username);
        Customer customer = customerService.getOne(qw);
        if (customer == null) {
            throw new BusinessException("Invalid or expired reset token.");
        }
        customer.setPassword(new BCryptPasswordEncoder().encode(newPass));
        customerService.update(customer, qw);
        redisCache.deleteObject(redisKey);
        return BackMsg.success("Password has been reset successfully.");
    }
}
