package com.gymmaster.mail;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Customer;
import com.gymmaster.service.CustomerService;
import com.gymmaster.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class CapController {

    @Value("${spring.mail.username:example@example.com}")
    private String mailUsername;

    @Value("${gymmaster.support-email:${spring.mail.username:example@example.com}}")
    private String supportEmail;

    @Autowired
    MailService mailService;
    @Autowired
    RedisCache redisCache;

    @Autowired
    CustomerService customerService;

    @GetMapping("/getCaptcha")
    public BackMsg<String> hello(String email) {
        int x = (int)((Math.random()*9+1)*100000);
        String regards = """
                Dear Sir/Madam,Thank you for choosing our service. We are excited to have you on board! As part of our account verification process, we need to confirm your email. To do this, we've sent you an SMS code to your registered phone number. However, if you're unable to receive SMS messages in your email box, please try again.
                
                Please find your SMS code below:
                
                %s
                If you have any trouble using the code, please don't hesitate to contact our support team at %s.
                Thank you for choosing our service. We look forward to serving you.
                Best regards.
                """.formatted(x, supportEmail);

        try {
            mailService.sendSimpleMail(mailUsername,
                    email,
                    email,
                    "This is your captcha, whose expire time is 1 minute",
                    regards);

        }
        catch (Exception e){
            return BackMsg.error("message cannot be sent successfully, please check the format of your email.");
        }
        redisCache.setCacheObject(mailUsername, String.valueOf(x), 5, TimeUnit.MINUTES);
        return BackMsg.success(String.valueOf(x));
    }

    // for reset password
    @GetMapping("/getCaptchaReset")
    public BackMsg<String> reset(String email, String Username) {
        // check if the username is valid
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getUsername, Username);
        // if the username does not exist
        if (customerService.getOne(wrapper) == null) {
            return BackMsg.error("username does not exist! Register first!");
        }
        // else, check if the email and username match
        if (!customerService.getOne(wrapper).getEmail().equals(email)) {
            return BackMsg.error("email does not match username!");
        }

        int x = (int)((Math.random()*9+1)*100000);
        String regards = """
            Dear Sir/Madam,Thank you for choosing our service. We are excited to have you on board! As part of our account verification process, we need to confirm your email. To do this, we've sent you an SMS code to your registered phone number. However, if you're unable to receive SMS messages in your email box, please try again.
                
            Please find your SMS code below:
                
            %s
            If you have any trouble using the code, please don't hesitate to contact our support team at %s.
            Thank you for choosing our service. We look forward to serving you.
            Best regards.
            """.formatted(x, supportEmail);

        try {
            mailService.sendSimpleMail(mailUsername,
                    email,
                    email,
                    "This is your captcha, whose expire time is 1 minute",
                    regards);

        }
        catch (Exception e){
            return BackMsg.error("message cannot be sent successfully, please check the format of your email.");
        }
        redisCache.setCacheObject(Username, String.valueOf(x), 5, TimeUnit.MINUTES);
        return BackMsg.success(String.valueOf(x));
    }


}

