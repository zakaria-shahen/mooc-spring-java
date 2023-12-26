package com.example.mooc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void send(String to, String body) {

        logger.debug("send email to {} and body: {}", to, body);
        // TODO: enable email server.
        if (true) {
            return;
        }
        var simpleMailMessage = new SimpleMailMessage();
         simpleMailMessage.setTo(to);
         simpleMailMessage.setFrom(from);
         simpleMailMessage.setSubject("confirm reset password");
         simpleMailMessage.setText(body);

        mailSender.send(simpleMailMessage);
    }
}
