package com.example.KinetoWebsite.Config;

import com.example.KinetoWebsite.Service.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "evsebiu@gmail.com";
    private static final String EMAIL_PASSWORD = "qoqp=iojkbnjkio8t7r6uyfgihit7r6dtyyqfpzbi"; // Use app-specific password
    private static final String ADMIN_EMAIL = "evsebiu@gmail.com";

    @Bean
    public EmailService emailService() {
        EmailService emailService = new EmailService();
        emailService.setHost(SMTP_HOST);
        emailService.setPort(SMTP_PORT);
        emailService.setUsername(EMAIL_USERNAME);
        emailService.setPassword(EMAIL_PASSWORD);
        emailService.setAdminEmail(ADMIN_EMAIL);
        emailService.setAuth(true);
        return emailService;
    }

    public static String getAdminEmail() {
        return ADMIN_EMAIL;
    }
}