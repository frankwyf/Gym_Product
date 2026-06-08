package com.gymmaster.mail.aa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username:example@example.com}")
    private String from;

    private String subject = "bill list";


    @Override
    public void sendMail(String to, String src) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            //第2个参数:是否允许添加多部件
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
//            helper.setText(content);
            //第2个参数:是否解析html
            helper.setText("The attached pdf is your bill list, thanks for your choosing us!");

            //添加附件
            File file = new File(src);
            //helper.addAttachment(file.getName(),file);
            helper.addAttachment(file.getName(),file);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
