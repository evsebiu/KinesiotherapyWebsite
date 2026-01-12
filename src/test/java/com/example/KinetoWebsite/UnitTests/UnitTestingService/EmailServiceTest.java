package com.example.KinetoWebsite.UnitTests.UnitTestingService;

import com.example.KinetoWebsite.Service.EmailService;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/*
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp(){
        emailService = new EmailService();
        emailService.setHost("mail.smtp.host");
        emailService.setPort("mail.smtp.port");
        emailService.setUsername("test@test.ro");
        emailService.setPassword("password123");
        emailService.setAdminEmail("admin@test.ro");
        emailService.setAuth(true);
    }



    @Test
    void sendAdminNotification_ShouldSendEmailSuccessfully() {
        MockedStatic<Session> sessionMock = mockStatic(Session.class);
        MockedStatic<Transport> transportMock = mockStatic(Transport.class);

        try {

            Properties realProperties = new Properties();
            realProperties.put("mail.smtp.host", "smtp.test.com");
            realProperties.put("mail.smtp.port", "587");
            realProperties.put("mail.smtp.auth", "true");
            realProperties.put("mail.smtp.starttls.enable", "true");

            //real session with props.
            Session realSession = Session.getInstance(realProperties, null);

            sessionMock.when(()->Session.getInstance(any(Properties.class),any()))
                    .thenReturn(realSession);

            //execute and test
            assertDoesNotThrow(()->emailService.sendAdminNotification("Test subject", "Test body"));

        } finally {
            // Close the static mocks
            sessionMock.close();
            transportMock.close();
        }
    }
    @Test
    void sendAdminHtmlNotification_ShouldSendHtmlEmailSuccessfully(){
        MockedStatic<Session> sessionMock = mockStatic(Session.class);
        MockedStatic<Transport> transportMock = mockStatic(Transport.class);

        try{
            Properties realProperties = new Properties();
            realProperties.put("mail.smtp.host" , "smtp.test.com");
            realProperties.put("mail.smtp.port", "587");
            realProperties.put("mail.smtp.auth", "true");
            realProperties.put("mail.smtp.starttls.enable", "true");

            Session realSession = Session.getInstance(realProperties, null);
            sessionMock.when(()->Session.getInstance(any(Properties.class), any()))
                    .thenReturn(realSession);

            assertDoesNotThrow(()-> emailService.sendAdminNotification("Test subject", "Test body"));
        } finally {
            sessionMock.close();
            transportMock.close();
        }
    }

    @Test
    void sendAdminNotification_ShouldHandleTransportException(){
        MockedStatic<Session> sessionMock = mockStatic(Session.class);
        MockedStatic<Transport> transportMock = mockStatic(Transport.class);

        try{
            Properties realProperties = new Properties();
            realProperties.put("mail.smtp.host", "smtp.test.com");
            realProperties.put("mail.smtp.port", "687");
            realProperties.put("mail.smtp.auth", "true");
            realProperties.put("mail.smtp.starttls.enable", "true");

            Session realSession = Session.getInstance(realProperties, null);
            sessionMock.when(()->Session.getInstance(any(Properties.class), any()))
                    .thenReturn(realSession);

            // mock transport.send and throw exception.
            transportMock.when(()->Transport.send(any(MimeMessage.class)))
                    .thenThrow(new RuntimeException("Transport error."));


            //should not throw exception - method should handle it internally
            assertDoesNotThrow(()-> emailService.sendAdminNotification("Test Subject", "Test body"));
        } finally {
            sessionMock.close();
            transportMock.close();
        }
    }


}
*/