package com.timmy.employee_management_system.service.impl;
import com.timmy.employee_management_system.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    @Override
    public void sendEmailWithAttachment(String to, ByteArrayInputStream file) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Employee Report");
        helper.setText("Attached is the employee report.");

        InputStreamSource attachment = new InputStreamSource() {
            @Override
            public InputStream getInputStream(){
                return file;
            }
        };
        helper.addAttachment("employees.xlsx", attachment);
        javaMailSender.send(message);
    }
}
